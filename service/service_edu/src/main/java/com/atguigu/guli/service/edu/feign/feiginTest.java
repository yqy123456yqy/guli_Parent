package com.atguigu.guli.service.edu.feign;

import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.service.edu.feign.fallback.OssFileServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "service-oss",fallback = OssFileServiceFallBack.class)
public interface feiginTest {

    @GetMapping("/admin/oss/file/test")
     void test();


    @DeleteMapping("/admin/oss/file/remove")
    R deleteFile(@RequestBody String url);





    }
