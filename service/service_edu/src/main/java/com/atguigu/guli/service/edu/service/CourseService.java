package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.vo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
public interface CourseService extends IService<Course> {

    String saveCourseInfoForm(CourseInfoForm courseInfoForm);

    CourseInfoForm getCourseById(String id);

    void updateById(CourseInfoForm courseInfoForm);

    IPage<CourseVo> selectPage(Long page, Long limit, CourseQueryVO courseQueryVO);

    Boolean removeCourseById(String id);

    Boolean removeCourseCover(String id);

    CoursePublishVo getCoursePublishVoById(String id);

    boolean updateCourseStatus(String id);

    List<Course> selectWebQueryList(WebCourseQueryVo webCourseQueryVo);

    WebCourseVo selectWebCourseById(String courseId);

    List<Course> selectCourse();

    CourseDto getCourseDtoById(String courseId);

    void updateBuyCount(String courseId);
}
