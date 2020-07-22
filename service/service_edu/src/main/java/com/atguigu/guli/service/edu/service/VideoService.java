package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author Helen
 * @since 2020-04-03
 */
public interface VideoService extends IService<Video> {



    void removeMedia(String id);

    void removeVod(String id);

    void removeCourseVod(String id);

}
