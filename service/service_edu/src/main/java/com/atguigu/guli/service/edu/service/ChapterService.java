package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Chapter;
import com.atguigu.guli.service.edu.entity.vo.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author Helen
 * @since 2020-04-03
 */
public interface ChapterService extends IService<Chapter> {

    List<ChapterVo> getChapterAndVideo(String id);

    boolean removChapter(String id);

}
