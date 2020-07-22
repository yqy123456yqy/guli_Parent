package com.atguigu.service.vod.Controller;
import com.aliyuncs.exceptions.ClientException;
import com.atguigu.guli.common.base.result.utils.ExceptionUtils;
import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.common.base.result.result.ResultCodeEnum;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.service.vod.service.VideoService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("/admin/vod/media")
@Slf4j
public class VodController {

    @Autowired
   private VideoService videoService;

    @PostMapping("uploadvod")
    public R uploadvod(@ApiParam(value = "file",required = true) @RequestParam("file") MultipartFile file){
        System.out.println("file = " + file);

        try {
            String vodId = videoService.uploadVideo(file.getInputStream(), file.getOriginalFilename());
            System.out.println("vodId = " + vodId);
            return R.ok().data("vodId",vodId).message("上传成功");
        } catch (IOException e) {
            log.error("上传失败"+ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_TOMCAT_ERROR);
        }
    }
    @DeleteMapping("removeVod/{vodId}")
    public R removeVod(@ApiParam(value = "视频Id",required = true)@PathVariable String vodId){
        System.out.println("vodId34 = " + vodId);

        try {
            videoService.removeVideo(vodId);
            return R.ok().message("删除视频成功");
        } catch (ClientException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.FILE_DELETE_ERROR);
        }
    }

    @DeleteMapping("/removes")
    public R removes(@ApiParam(value = "视频Id",required = true)@RequestBody List<String> ids){

        try {
            videoService.removeVods(ids);
            return R.ok().message("批量删除成功");
        } catch (ClientException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }


    }
}
