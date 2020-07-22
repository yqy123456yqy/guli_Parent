package com.atguigu.service.sms.controller.api;
import com.aliyuncs.exceptions.ClientException;
import com.atguigu.guli.common.base.result.utils.FormUtils;
import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.common.base.result.utils.RandomUtils;
import com.atguigu.guli.common.base.result.result.ResultCodeEnum;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.service.sms.service.SmsService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/sms")
@Slf4j
public class SmsController {

    @Autowired
    SmsService smsService;

    @Autowired
    private RedisTemplate redisTemplate;



    @GetMapping("/send/{mobile}")
    public R getCode(@ApiParam(value = "用户手机号码",required = true) @PathVariable String mobile) throws ClientException {

        if(StringUtils.isEmpty(mobile) || !FormUtils.isMobile(mobile)){

            log.error("请输入正确的手机号码");
            throw new GuliException(ResultCodeEnum.LOGIN_PHONE_ERROR);
        }


        String checkCode = RandomUtils.getFourBitRandom();
        //smsService.send(mobile,checkCode);


        redisTemplate.opsForValue().set(mobile, checkCode,5, TimeUnit.MINUTES);

        return R.ok().message("短信发送成功");
    }
}
