package com.atguigu.guli.service.trade.service.impl;

import com.atguigu.guli.common.base.result.utils.ExceptionUtils;
import com.atguigu.guli.common.base.result.result.ResultCodeEnum;
import com.atguigu.guli.common.base.result.utils.HttpClientUtils;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.service.OrderService;
import com.atguigu.guli.service.trade.service.WeixinPayService;
import com.atguigu.guli.service.trade.utils.WeixinPayProperties;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@Service
public class WeixinPayServiceImpl implements WeixinPayService {

    @Autowired
    private OrderService orderService;

    @Autowired
    WeixinPayProperties weixinPayProperties;


    @Override
    public Map<String, Object> createNative(String orderNo, String remoteAddr) {

        try {
            Order order = orderService.getOrderByOrderNo(orderNo);

            HttpClientUtils client = new HttpClientUtils("https://api.mch.weixin.qq.com/pay/unifiedorder");


            Map<String,String> params = new HashMap<>();
            params.put("appid", weixinPayProperties.getAppId());
            params.put("mch_id", weixinPayProperties.getPartner());
            params.put("nonce_str", WXPayUtil.generateNonceStr());
            params.put("body", order.getCourseTitle());
            params.put("out_trade_no", order.getOrderNo());
            params.put("total_fee", order.getTotalFee().intValue()+"");
            params.put("spbill_create_ip", remoteAddr);
            params.put("notify_url", weixinPayProperties.getNotifyUrl());
            params.put("trade_type", "NATIVE");
            String xmlParams = WXPayUtil.generateSignedXml(params, weixinPayProperties.getPartnerKey());
            log.info(xmlParams);
            client.setXmlParam(xmlParams);
            client.setHttps(true);
            client.post();
            String resultXml = client.getContent();
            log.info(resultXml);
            Map<String, String> resultMap = WXPayUtil.xmlToMap(resultXml);

            if("FAIL".equals(resultMap.get("return_code"))
            || "FAIL".equals(resultMap.get("result_code"))){

                log.error("微信支付统一下单错误 - "
                        + "return_code: " + resultMap.get("return_code")
                        + "return_msg: " + resultMap.get("return_msg")
                        + "result_code: " + resultMap.get("result_code")
                        + "err_code: " + resultMap.get("err_code")
                        + "err_code_des: " + resultMap.get("err_code_des"));

                throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);

            }

            Map<String,Object> map = new HashMap<>();

            map.put("result_code", resultMap.get("result_code"));
            map.put("code_url", resultMap.get("code_url"));
            map.put("out_trade_no", orderNo);
            map.put("course_id", order.getCourseId());//课程id
            map.put("total_fee", order.getTotalFee());//订单总金额
            return map;


        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
        }


    }
}
