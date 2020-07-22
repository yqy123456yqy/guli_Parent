package com.atguigu.guli.service.edu.controller.api;
import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.edu.service.CourseService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/edu/course")
@Slf4j
public class ApiCourseController {

    @Autowired
    private CourseService courseService;


    @ApiOperation("通过课程Id查询课程及讲师")
    @GetMapping("inner/get-course-dto/{courseId}")
    public CourseDto getCourseDtoById(@PathVariable String courseId){

        CourseDto courseDto = courseService.getCourseDtoById(courseId);
        return courseDto;

    }

    @GetMapping("inner/update-buy-count/{courseId}")
    public R updateBuy(@PathVariable String courseId){

      courseService.updateBuyCount(courseId);

      return R.ok();
    }
}
