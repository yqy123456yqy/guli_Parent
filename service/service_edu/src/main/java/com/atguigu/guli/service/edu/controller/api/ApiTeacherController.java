package com.atguigu.guli.service.edu.controller.api;

import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.service.TeacherService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/edu/teacher")
@Slf4j
public class ApiTeacherController {

    @Autowired
    TeacherService teacherService;



    @GetMapping("/listAll")
    public R listAll(){
       List<Teacher> teachers =  teacherService.list(null);

       if(teachers != null){
           return R.ok().data("items",teachers);
       }else{
           return R.error().message("讲师获取失败");
       }
    }

    @GetMapping("/get/{id}")
    public R get(@ApiParam(value = "讲师的Id",required = true) @PathVariable String id){

        Map<String, Object> objectMap = teacherService.get(id);

        if(objectMap != null){
            return R.ok().data(objectMap);
        }else{
            return R.ok().message("数据不存在");
        }

    }



}
