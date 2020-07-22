package com.atguigu.guli.service.edu.feign.fallback;

import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.service.edu.feign.VodMediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class VodFileServiceFallBack implements VodMediaService {
    @Override
    public R removeVod(String vodId) {
        log.info("熔断保护");
        return R.error();
    }

    @Override
    public R removes(List<String> ids) {
        log.info("熔断保护");
        return R.error();
    }
}
