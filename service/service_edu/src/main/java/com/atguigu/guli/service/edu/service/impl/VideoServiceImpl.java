package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.mapper.VideoMapper;
import com.atguigu.guli.service.edu.service.VideoService;
import com.atguigu.guli.service.edu.feign.VodMediaService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2020-04-03
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Autowired
    VideoService videoService;

    @Autowired
    VodMediaService vodMediaService;

    @Override
    public void removeMedia(String id) {
        Video vodeo = videoService.getById(id);
        String videoSourceId = vodeo.getVideoSourceId();
        vodMediaService.removeVod(videoSourceId);

    }
    @Override
    public void removeVod(String id) {

        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("chapter_id", id);
        videoQueryWrapper.select("video_source_id");

        List<Map<String,Object>> maps = baseMapper.selectMaps(videoQueryWrapper);

        List<String> videoSourceIdList = new ArrayList<>();

        for (Map<String, Object> map : maps) {
            String videosourceid = (String) map.get("video_source_id");
            videoSourceIdList.add(videosourceid);
        }

        vodMediaService.removes(videoSourceIdList);



    }

    @Override
    public void removeCourseVod(String id) {
        QueryWrapper<Video> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.eq("course_id", id);
        courseQueryWrapper.select("video_source_id");

       List<Map<String, Object>> maps  = baseMapper.selectMaps(courseQueryWrapper);

       List<String> idsList = new ArrayList<>();

        for (Map<String, Object> map : maps) {
            String  videoSourseId = map.get("video_source_id").toString();
            idsList.add(videoSourseId);
        }

        vodMediaService.removes(idsList);


    }


}
