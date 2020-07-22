package com.atguigu.guli.service.statistics.task;

import com.atguigu.guli.service.statistics.service.DailyService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    @Autowired
    DailyService dailyService;

    //@Scheduled(cron = "0/1 * * * * ? ")
    public void test(){
        System.out.println("test执行");
    }
    @Scheduled(cron = "0 0 1 * * ?")
    public void taskGenarateStatisticsData(){
        String day = new DateTime().minusDays(1).toString("yyyy-MM-dd");
        dailyService.selectCount(day);
        System.out.println("taskGenarateStatisticsData 统计完毕");
    }
}
