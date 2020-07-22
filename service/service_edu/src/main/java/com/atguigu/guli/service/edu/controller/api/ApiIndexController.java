package com.atguigu.guli.service.edu.controller.api;

import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.service.CourseService;
import com.atguigu.guli.service.edu.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/edu/index")
public class ApiIndexController {

    @Autowired
   private TeacherService teacherService;

    @Autowired
   private CourseService courseService;


    @Autowired
   private RedisTemplate redisTemplate;

    @GetMapping
    public R index(){

      List<Teacher> teachers = teacherService.selectTeacher();

       List<Course> courses =  courseService.selectCourse();

       return R.ok().data("teacherList",teachers).data("courseList",courses);

    }





}
