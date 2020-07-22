package com.atguigu.guli.service.statistics.service.impl;

import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.common.base.result.utils.ExceptionUtils;
import com.atguigu.guli.service.statistics.entity.Daily;
import com.atguigu.guli.service.statistics.feign.UcenterService;
import com.atguigu.guli.service.statistics.mapper.DailyMapper;
import com.atguigu.guli.service.statistics.service.DailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.apache.velocity.runtime.directive.Foreach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2020-05-13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {

    @Autowired
    private UcenterService ucenterService;


    @Override
    public void selectCount(String day) {

        QueryWrapper<Daily> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date_calculated", day);
        this.remove(queryWrapper);

        R result = ucenterService.getCount(day);
        Integer registerNum = (Integer) result.getData().get("registerNum");
        int loginNum = RandomUtils.nextInt(100, 200);
        int videoViewNum = RandomUtils.nextInt(100, 200);
        int courseNum = RandomUtils.nextInt(100, 200);

        Daily daily = new Daily();
        daily.setCourseNum(courseNum);
        daily.setLoginNum(loginNum);
        daily.setRegisterNum(registerNum);
        daily.setVideoViewNum(videoViewNum);
        daily.setDateCalculated(day);

        baseMapper.insert(daily);

    }

    @Override
    public Map<String,Map<String,Object>> getChartData(String begin, String end) {

        Map<String,Map<String,Object>> map = new HashMap<>();

        Map<String, Object> register_num = getChartDataByType(begin, end, "register_num");
        Map<String, Object> login_num = getChartDataByType(begin, end, "login_num");
        Map<String, Object> video_view_num = getChartDataByType(begin, end, "video_view_num");
        Map<String, Object> course_num = getChartDataByType(begin, end, "course_num");

        map.put("register_num",register_num);
        map.put("login_num",login_num);
        map.put("video_view_num",video_view_num);
        map.put("course_num",course_num);

        return map;

    }

    public Map<String,Object> getChartDataByType(String begin,String end,String type){

        Map<String,Object> map = new HashMap<>();

        List<String> xAxis = new ArrayList<>();
        List<Integer> yAxis = new ArrayList<>();

        QueryWrapper<Daily> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("date_calculated",type);
        queryWrapper.between("date_calculated", begin, end);

        List<Map<String, Object>> maps = baseMapper.selectMaps(queryWrapper);


        for (Map<String, Object> objectMap : maps) {
            String date_calculated = (String)objectMap.get("date_calculated");
            xAxis.add(date_calculated);

            Integer count = (Integer)objectMap.get(type);
            yAxis.add(count);
        }
        map.put("xData",xAxis);
        map.put("yData", yAxis);
        return map;

    }
}
