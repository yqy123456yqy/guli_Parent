package com.atguigu.service.sms.controller.api;
import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.service.sms.utils.SmsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RefreshScope
@RestController
@RequestMapping("/sms")
public class SampleController {

    @Autowired
    RedisTemplate redisTemplate;


    @Autowired
    private SmsProperties smsProperties;

    @Value("${aliyun.sms.signName}")
    private String signName;

    @GetMapping("test")
    public R test(){

        return R.ok().data("signName",signName);
    }

    @GetMapping("test2")
    public R test2(){
       return R.ok().data("smsProperties",smsProperties);
    }

    @GetMapping("test3")
    public R test3(){
        redisTemplate.opsForValue().set("v1", "2v",5, TimeUnit.MINUTES);
        return R.ok();

    }
}
