package com.atguigu.service.sms.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.guli.common.base.result.result.ResultCodeEnum;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.service.sms.service.SmsService;
import com.atguigu.service.sms.utils.SmsProperties;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@Service
public class SmsServiceImpl implements SmsService {


    @Autowired
    private SmsProperties smsProperties;

    @Override
    public void send(String mobile, String checkCode) throws ClientException {

        DefaultProfile profile = DefaultProfile.getProfile(smsProperties.getRegionId(), smsProperties.getKeyId(), smsProperties.getKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", smsProperties.getRegionId());
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", smsProperties.getSignName());
        request.putQueryParameter("TemplateCode", smsProperties.getTemplateCode());

        Map<String,Object> map = new HashMap<>();
        map.put("code", checkCode);
        Gson gson = new Gson();


        request.putQueryParameter("TemplateParam", gson.toJson(map));

        CommonResponse response = client.getCommonResponse(request);
        String data = response.getData();
        HashMap<String,String> hashMap = gson.fromJson(data, HashMap.class);
        String code = hashMap.get("code");
        String message = hashMap.get("message");

        if("isv.BUSINESS_LIMIT_CONTROL".equals(code)){
            log.error("短信发送过于频繁：【code】"+code+"【message】"+message);
            throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL);
        }

        if("ok".equals(code)){
            log.error("短信发送失败：【code】"+code+"【message】"+message);
            throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR);
        }




    }
}
