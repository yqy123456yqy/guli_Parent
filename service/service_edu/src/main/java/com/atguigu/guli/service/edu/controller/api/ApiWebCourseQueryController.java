package com.atguigu.guli.service.edu.controller.api;

import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.vo.WebCourseQueryVo;
import com.atguigu.guli.service.edu.service.CourseService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/edu/course")
@Slf4j
public class ApiWebCourseQueryController {

    @Autowired
    CourseService courseService;

    @GetMapping("/list")
    public R getCourseList(@ApiParam(value = "课程",required = false) WebCourseQueryVo webCourseQueryVo){
        System.out.println("webCourseQueryVo = " + webCourseQueryVo);

      List<Course> courses = courseService.selectWebQueryList(webCourseQueryVo);

      if(courses != null){
          return R.ok().data("items",courses);
      }else{
          return R.error().message("数据失败");
      }

    }
}
