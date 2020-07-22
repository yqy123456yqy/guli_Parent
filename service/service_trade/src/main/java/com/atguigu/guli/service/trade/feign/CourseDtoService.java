package com.atguigu.guli.service.trade.feign;

import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.service.base.dto.CourseDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(value = "service-edu")
public interface CourseDtoService {
    @ApiOperation("通过课程Id查询课程及讲师")
    @GetMapping("/api/edu/course/inner/get-course-dto/{courseId}")
    CourseDto getCourseDtoById(@PathVariable String courseId);


    @GetMapping("/api/edu/course/inner/update-buy-count/{courseId}")
    R updateBuy(@PathVariable String courseId);
}
