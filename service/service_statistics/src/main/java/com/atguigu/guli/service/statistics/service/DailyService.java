package com.atguigu.guli.service.statistics.service;

import com.atguigu.guli.service.statistics.entity.Daily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author Helen
 * @since 2020-05-13
 */
public interface DailyService extends IService<Daily> {


    void selectCount(String day);

    Map<String,Map<String,Object>> getChartData(String begin, String end);
}
