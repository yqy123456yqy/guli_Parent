package com.atguigu.service.vod.Controller.api;

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

@Slf4j
@RestController
@RequestMapping("/api/edu/media")
public class ApiMediaController {

    @Autowired
    private VideoService videoService;



    @GetMapping("get-play-auth/{videoSourceId}")
    public R getPalyAuth(@ApiParam(value = "视频Id",required = true) @PathVariable String videoSourceId){
        System.out.println("videoSourceId = " + videoSourceId);

        try {
            String playAuth = videoService.selectPlayAuth(videoSourceId);
            System.out.println("playAuth = " + playAuth);
            return R.ok().data("playAuth",playAuth);
        } catch (ClientException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.FETCH_PLAYAUTH_ERROR);
        }


    }

}