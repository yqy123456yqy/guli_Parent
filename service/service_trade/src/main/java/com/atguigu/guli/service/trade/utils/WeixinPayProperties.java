package com.atguigu.guli.service.trade.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("weixin.pay")
public class WeixinPayProperties {
    private String appId;
    private String partner;
    private String partnerKey;
    private String notifyUrl;
}
