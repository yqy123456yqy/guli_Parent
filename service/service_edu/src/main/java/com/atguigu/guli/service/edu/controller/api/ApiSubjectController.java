package com.atguigu.guli.service.edu.controller.api;

import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.service.edu.entity.vo.SubjectVo;
import com.atguigu.guli.service.edu.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/edu/subject")
public class ApiSubjectController {

    @Autowired
    SubjectService subjectService;

    @GetMapping("listSubject")
    public R getSubject(){

     List<SubjectVo> subjectVos = subjectService.nestedList();
         return R.ok().data("items",subjectVos);

    }
}
