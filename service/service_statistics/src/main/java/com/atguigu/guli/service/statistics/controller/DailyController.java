package com.atguigu.guli.service.statistics.controller;
import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.service.statistics.service.DailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2020-05-13
 */
@RestController
@RequestMapping("/admin/statistics/daily")
public class DailyController {

    @Autowired
    private DailyService dailyService;

    @PostMapping("create/{day}")
    public R getCount(@PathVariable String day){

        dailyService.selectCount(day);
        return R.ok().message("生成统计");
    }

    @GetMapping("show-chart/{begin}/{end}")
    public R showChart(@PathVariable String begin,@PathVariable String end){

        Map<String,Map<String,Object>> chartData =  dailyService.getChartData(begin,end);

        return R.ok().data("chartData",chartData);

    }

}

