package com.atguigu.guli.service.trade.feign.fallback;

import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.trade.feign.CourseDtoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CourseDtoServiceImpl implements CourseDtoService {
    @Override
    public CourseDto getCourseDtoById(String courseId) {
        log.info("熔断保护");
        return null;
    }

    @Override
    public R updateBuy(String courseId) {
        log.info("熔断保护");
        return R.error();

    }
}
