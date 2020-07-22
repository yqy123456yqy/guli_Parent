package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVo;
import com.atguigu.guli.service.edu.entity.vo.CourseQueryVO;
import com.atguigu.guli.service.edu.entity.vo.CourseVo;
import com.atguigu.guli.service.edu.service.CourseService;
import com.atguigu.guli.service.edu.service.VideoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2020-04-03
 */

@RestController
@Api(description = "课程管理")
@RequestMapping("/admin/edu/course")
public class CourseController {

    @Autowired
    CourseService courseService;

    @Autowired
    VideoService videoService;

    @PostMapping("/save-course-info")
    public R saveCourse(@ApiParam(value = "课程基本信息",required = true) @RequestBody CourseInfoForm courseInfoForm){
        String courseId = courseService.saveCourseInfoForm(courseInfoForm);

        return R.ok().data("courseId",courseId).message("保存成功");
    }
    @GetMapping("/getCourseById/{id}")
    public R getCourseById(@ApiParam(value = "课程ID",required = true) @PathVariable String id){
      CourseInfoForm courseInfoForm = courseService.getCourseById(id);
        if(courseInfoForm != null){
            return R.ok().data("items",courseInfoForm);
        }else{
            return R.error().message("数据不存在");
        }
    }

    @PutMapping("/update")
    public R update(@ApiParam(value = "课程基本信息",required = true)@RequestBody CourseInfoForm courseInfoForm){

        courseService.updateById(courseInfoForm);
        return R.ok().message("修改成功");
    }

    @ApiOperation("分页课程列表")
    @GetMapping("/list/{page}/{limit}")
    public R index(@ApiParam(value = "当前页码",required = true)@PathVariable Long page, @ApiParam(value = "每页记录数",required = true)@PathVariable Long limit,@ApiParam(value = "查询数据")  CourseQueryVO courseQueryVO){

        System.out.println("courseQueryVO="+courseQueryVO);
     IPage<CourseVo> courseVoIPage =  courseService.selectPage(page,limit,courseQueryVO);
        List<CourseVo> records = courseVoIPage.getRecords();
        System.out.println(records);
        long total = courseVoIPage.getTotal();
        return R.ok().data("items",records).data("total",total);
    }
    @DeleteMapping("/remove/{id}")
    public R remove(@ApiParam(value = "课程ID",required = true) @PathVariable String id){
        videoService.removeCourseVod(id);

        courseService.removeCourseCover(id);

        Boolean flag = courseService.removeCourseById(id);
        if(flag){
            return R.ok().message("删除成功");
        }else{
            return R.error().message("删除失败");
        }

    }
    @ApiOperation("根据Id获取要发布课程的信息")
    @GetMapping("/getCoursePublishVo/{id}")
    public R getCoursePublishVo(@ApiParam(value = "课程ID",required = true) @PathVariable String id ){

        CoursePublishVo coursePublishVo =  courseService.getCoursePublishVoById(id);
            if(coursePublishVo != null){
                return R.ok().data("items",coursePublishVo);
            }else{
                return R.error().message("数据不存在");
            }
    }
    @ApiOperation("根据Id修改发布状态")
    @PutMapping("/updateStatus/{id}")
    public R updateStatus(@ApiParam(value = "课程Id",required = true) @PathVariable String id){
      boolean result =  courseService.updateCourseStatus(id);
      if(result){
          return R.ok().message("发布成功");
      }else{
          return R.error().message("发布失败");
      }

    }


}

