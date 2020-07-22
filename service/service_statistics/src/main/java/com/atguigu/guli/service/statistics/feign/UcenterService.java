package com.atguigu.guli.service.statistics.feign;

import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.service.statistics.feign.fallback.UcenterServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(value = "service-ucenter",fallback = UcenterServiceFallBack.class)
public interface UcenterService {

    @GetMapping("/admin/ucenter/member/count-register-num/{day}")
    R getCount(@PathVariable String day);

}
