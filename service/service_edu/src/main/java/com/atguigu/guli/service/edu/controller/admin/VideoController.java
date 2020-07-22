package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2020-04-03
 */
@Api(description = "视频管理")
@RestController
@RequestMapping("/admin/edu/video")
public class VideoController {

    @Autowired
    VideoService videoService;

    @DeleteMapping("/removeVideo/{id}")
    public R removeVideo(@ApiParam(value = "VideoID",required = true) @PathVariable String id) {

        videoService.removeMedia(id);


        boolean result = videoService.removeById(id);
        if(result){
            return R.ok().message("删除成功");
        }else{
            return R.error().message("删除失败");
        }


    }
    @PostMapping("/saveVideo")
    public R saveVideo(@ApiParam(value = "视频",required = true) @RequestBody Video video){

        boolean result = videoService.save(video);
        if(result){
            return R.ok().message("保存成功");
        }else{
            return R.error().message("保存失败");
        }

    }
    @GetMapping("/getVideo/{id}")
    public R getVideo(@ApiParam(value = "视频ID",required = true) @PathVariable String id){

        Video video = videoService.getById(id);
        System.out.println("video1 = " + video);
        if(video != null){
            return R.ok().data("items",video);
        }else{
            return R.error().message("数据不存在");
        }

    }
    @PutMapping("/updateVideo")
    public R updateVideo(@ApiParam(value = "视频",required = true) @RequestBody Video video){

        boolean result = videoService.updateById(video);
        if(result){
            return R.ok().message("修改成功");
        }else{
            return R.error().message("修改失败");
        }

    }
}

