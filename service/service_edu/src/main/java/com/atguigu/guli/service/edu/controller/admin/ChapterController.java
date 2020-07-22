package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.service.edu.entity.Chapter;
import com.atguigu.guli.service.edu.entity.vo.ChapterVo;
import com.atguigu.guli.service.edu.service.ChapterService;
import com.atguigu.guli.service.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
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
@Api(description = "课程")
@Slf4j
@RestController
@RequestMapping("/admin/edu/chapter")
public class ChapterController {

    @Autowired
    ChapterService chapterService;

    @Autowired
    VideoService videoService;

    @GetMapping("/getChapter/{id}")
    public R getChapter(@ApiParam(value = "课程的ID",required = true)@PathVariable String id){
        System.out.println("id = " + id);
       List<ChapterVo>  chapterVo = chapterService.getChapterAndVideo(id);
        if(chapterVo != null){
            return R.ok().data("items",chapterVo);
        }else{
            return R.error().message("数据不存在");
        }

    }

    @DeleteMapping("/removeChapter/{id}")
    public R removeChapter(@ApiParam(value = "章节Id", required = true) @PathVariable String id){

        videoService.removeVod(id);

        boolean result = chapterService.removChapter(id);


       if(result){
           return R.ok().message("删除成功");
       }else{
           return R.error().message("删除失败");
       }

    }

    @PostMapping("/saveChapter")
    public R saveVhapter(@ApiParam(value = "章节",required = true)@RequestBody Chapter chapter){

        boolean result = chapterService.save(chapter);
        if(result){
            return R.ok().message("保存成功");
        }else{
            return R.error().message("保存失败");
        }

    }
    @GetMapping("/getChapters/{id}")
    public R getChapters(@ApiParam(value = "章节Id",required = true)@PathVariable String id){

        Chapter chapter = chapterService.getById(id);
        if(chapter != null){
            return R.ok().data("items",chapter);
        }else{
            return R.error().message("数据不存在");
        }

    }
    @PutMapping("/updateChapter")
    public R updateChapter(@ApiParam(value = "章节",required = true) @RequestBody Chapter chapter){

        boolean result = chapterService.updateById(chapter);
        if(result){
            return R.ok().message("修改成功");
        }else{
            return R.error().message("修改失败");
        }
    }



}

