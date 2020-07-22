package com.atguigu.guli.service.edu.feign;

import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.service.edu.feign.fallback.VodFileServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "service-vod",fallback = VodFileServiceFallBack.class)
public interface VodMediaService {

    @DeleteMapping("/admin/vod/media/removeVod/{vodId}")
    R removeVod(@PathVariable("vodId") String vodId);

    @DeleteMapping("/admin/vod/media/removes")
    R removes(@RequestBody List<String> ids);


    }
