package com.atguigu.guli.service.edu.controller.api;
import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.service.edu.entity.vo.ChapterVo;
import com.atguigu.guli.service.edu.entity.vo.WebCourseVo;
import com.atguigu.guli.service.edu.service.ChapterService;
import com.atguigu.guli.service.edu.service.CourseService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/edu/courseVo")

public class ApiWebCourseController {

    @Autowired
    CourseService courseService;

    @Autowired
    ChapterService chapterService;

    @GetMapping("/get/{courseId}")
    public R getCourseId(@ApiParam(value = "课程id",required = true) @PathVariable String courseId){

       WebCourseVo webCourseVo = courseService.selectWebCourseById(courseId);

        List<ChapterVo> chapterAndVideo = chapterService.getChapterAndVideo(courseId);
        System.out.println("chapterAndVideo = " + chapterAndVideo);
        return R.ok().data("course",webCourseVo).data("chapterVoList",chapterAndVideo);


    }
}
