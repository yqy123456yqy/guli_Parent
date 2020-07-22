package com.atguigu.guli.service.statistics.feign.fallback;

import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.service.statistics.feign.UcenterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class UcenterServiceFallBack implements UcenterService {


    @Override
    public R getCount(String day) {

      log.error("熔断保护");
      return R.ok().data("registerNum",0);
    }
}
