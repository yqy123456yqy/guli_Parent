package com.atguigu.guli.service.trade.controller.api;

import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.common.base.result.result.ResultCodeEnum;
import com.atguigu.guli.common.base.result.utils.StreamUtils;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.service.OrderService;
import com.atguigu.guli.service.trade.service.WeixinPayService;
import com.atguigu.guli.service.trade.utils.WeixinPayProperties;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/trade/weixin-pay")
@Slf4j
public class ApiWeixinPayController {
    @Autowired
    WeixinPayService weixinPayService;

    @Autowired
    WeixinPayProperties weixinPayProperties;

    @Autowired
    OrderService orderService;

    @GetMapping("create-native/{orderNo}")
    public R createNative(@PathVariable String orderNo, HttpServletRequest request) {
        System.out.println("orderNo = " + orderNo);
        String remoteAddr = request.getRemoteAddr();

        Map<String, Object> map = weixinPayService.createNative(orderNo, remoteAddr);

        return R.ok().data("map", map);
    }

    @PostMapping("callback/notify")
    public String callback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("callback/notify 被调用");
        ServletInputStream inputStream = request.getInputStream();
        String notifyXml = StreamUtils.inputStream2String(inputStream, "utf-8");

        System.out.println("notifyXml = " + notifyXml);

        Map<String, String> resultMap = new HashMap<>();

        if (WXPayUtil.isSignatureValid(notifyXml, weixinPayProperties.getPartnerKey())) {

            Map<String, String> map = WXPayUtil.xmlToMap(notifyXml);

            //判断支付是否成功
            if ("SUCCESS".equals(map.get("result_code"))) {

                // 校验订单金额是否一致
                String total_fee = map.get("total_fee");

                String out_trade_no = map.get("out_trade_no");

                Order order = orderService.getOrderByOrderNo(out_trade_no);

                if (order != null && order.getTotalFee().intValue() == Integer.parseInt(total_fee)) {

                    // 判断订单状态：保证接口调用的幂等性，如果订单状态已更新直接返回成功响应
                    // 幂等性：无论调用多少次结果都是一样的
                    if (order.getStatus() == 1) {
                        resultMap.put("return_code", "SUCCESS");
                        resultMap.put("return_msg", "OK");
                        String returnXml = WXPayUtil.mapToXml(resultMap);
                        response.setContentType("text/xml");
                        log.info("支付成功，通知已处理");
                        return returnXml;
                    } else {
                        orderService.updateOrderStutas(map);
                        resultMap.put("return_code", "SUCCESS");
                        resultMap.put("return_msg", "OK");
                        String mapToXml = WXPayUtil.mapToXml(resultMap);
                        response.setContentType("text/xml");
                        return mapToXml;

                    }
                }

            }
        }

        resultMap.put("return_code", "FAIL");
        resultMap.put("return_msg", "");
        String mapToXml = WXPayUtil.mapToXml(resultMap);
        response.setContentType("text/xml");
        log.warn("校验失败");
        return mapToXml;
    }

    @GetMapping("/query-pay-status/{orderNo}")
    public R getStatus(@PathVariable String orderNo) {

        boolean result = orderService.queryPayStatus(orderNo);

        if (result) {
            return R.ok().message("支付成功");
        }

        return R.setResult(ResultCodeEnum.PAY_RUN);
    }


}
