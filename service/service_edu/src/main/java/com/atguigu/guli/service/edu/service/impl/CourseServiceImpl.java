package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.edu.entity.*;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.vo.*;
import com.atguigu.guli.service.edu.mapper.*;
import com.atguigu.guli.service.edu.service.CourseService;
import com.atguigu.guli.service.edu.feign.feiginTest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2020-04-03
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
    @Autowired
    CourseDescriptionMapper courseDescriptionMapper;

    @Autowired
    VideoMapper videoMapper;

    @Autowired
    ChapterMapper chapterMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    CourseCollectMapper courseCollectMapper;

    @Autowired
    feiginTest feiginTests;

    @Override
    public String saveCourseInfoForm(CourseInfoForm courseInfoForm) {


        Course course = new Course();

        BeanUtils.copyProperties(courseInfoForm, course);
        course.setStatus(Course.COURSE_DRAFT);

        baseMapper.insert(course);
        String courseId = course.getId();


        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setId(course.getId());

        courseDescription.setDescription(courseInfoForm.getDescription());

        courseDescriptionMapper.insert(courseDescription);
        return courseId;
    }

    @Override
    public CourseInfoForm getCourseById(String id) {

        Course course = baseMapper.selectById(id);
        if(course == null){
            return null;
        }
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        BeanUtils.copyProperties(course, courseInfoForm);

        CourseDescription courseDescription = courseDescriptionMapper.selectById(id);
        courseInfoForm.setDescription(courseDescription.getDescription());

        return courseInfoForm;
    }

    @Override
    public void updateById(CourseInfoForm courseInfoForm) {
        Course course = new Course();
        BeanUtils.copyProperties(courseInfoForm, course);
        baseMapper.updateById(course);
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(course.getId());
        courseDescriptionMapper.updateById(courseDescription);

    }

    @Override
    public IPage<CourseVo> selectPage(Long page, Long limit, CourseQueryVO courseQueryVO) {
        QueryWrapper<CourseVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("c.gmt_create");

        String title = courseQueryVO.getTitle();
        String teacherId = courseQueryVO.getTeacherId();
        String subjectId = courseQueryVO.getSubjectId();
        String subjectParentId = courseQueryVO.getSubjectParentId();

        if(!StringUtils.isEmpty(title)){
            queryWrapper.like("c.title", title);
        }
        if(!StringUtils.isEmpty(teacherId)){
            queryWrapper.eq("t.id", teacherId);
        }
        if(!StringUtils.isEmpty(subjectId)){
            queryWrapper.eq("s.id", subjectId);
        }
        if(!StringUtils.isEmpty(subjectParentId)){
            queryWrapper.eq("s2.id", subjectParentId);
        }

        Page<CourseVo> courseVoPage = new Page<>(page, limit);
        List<CourseVo> records =  baseMapper.selectPageByCourseQueryVo(courseVoPage,queryWrapper);

        return courseVoPage.setRecords(records);


    }

    @Override
    public Boolean removeCourseById(String id) {

        //课时信息：video
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id", id);
        videoMapper.delete(videoQueryWrapper);

        //章节信息：chapter
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id", id);
        chapterMapper.delete(chapterQueryWrapper);
        //评论信息：comment
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("course_id", id);
        commentMapper.delete(commentQueryWrapper);

        //收藏信息：course_collect
        QueryWrapper<CourseCollect> courseCollectQueryWrapper = new QueryWrapper<>();
        courseCollectQueryWrapper.eq("course_id", id);
        courseCollectMapper.delete(courseCollectQueryWrapper);
        //课程详情：course_description
        courseDescriptionMapper.deleteById(id);
        //课程信息：course
        return this.removeById(id);




    }

    @Override
    public Boolean removeCourseCover(String id) {

        Course course = baseMapper.selectById(id);
        if(course != null){
            String cover = course.getCover();
            if(!StringUtils.isEmpty(cover)){
                R result = feiginTests.deleteFile(cover);
                return result.getSuccess();

            }
        }

        return false;
    }

    @Override
    public CoursePublishVo getCoursePublishVoById(String id) {


        return baseMapper.selectCoursePublishVoById(id);


    }

    @Override
    public boolean updateCourseStatus(String id) {
       Course course = new Course();
       course.setId(id);
       course.setStatus("warning");
      return this.updateById(course);

    }

    @Override
    public List<Course> selectWebQueryList(WebCourseQueryVo webCourseQueryVo) {

        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.eq("status", Course.COURSE_NORMAL);

        if(!StringUtils.isEmpty(webCourseQueryVo.getSubjectParentId())){
            courseQueryWrapper.eq("subject_parent_id", webCourseQueryVo.getSubjectParentId());
        }

        if(!StringUtils.isEmpty(webCourseQueryVo.getSubjectId())){
            courseQueryWrapper.eq("subject_id", webCourseQueryVo.getSubjectId());
        }

        if(!StringUtils.isEmpty(webCourseQueryVo.getBuyCountSort())){
            courseQueryWrapper.orderByDesc("buy_count");
        }

        if(!StringUtils.isEmpty(webCourseQueryVo.getPriceSort())){
            if(webCourseQueryVo.getType() == 1 || webCourseQueryVo.getType() == null){
                courseQueryWrapper.orderByAsc("price");
            }else{
                courseQueryWrapper.orderByDesc("price");
            }
        }

        if(!StringUtils.isEmpty(webCourseQueryVo.getGmtCreateSort())){
            courseQueryWrapper.orderByDesc("gmt_create");
        }


        return baseMapper.selectList(courseQueryWrapper);



    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public WebCourseVo selectWebCourseById(String courseId) {

        Course course = baseMapper.selectById(courseId);
        course.setViewCount(course.getViewCount()+1);
        baseMapper.updateById(course);

        return baseMapper.selectWebCourseVo(courseId);



    }

    @Cacheable(value = "index",key = "'selectCourse'")
    @Override
    public List<Course> selectCourse() {

        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.orderByDesc("view_count");
        courseQueryWrapper.last("limit 8");
       return baseMapper.selectList(courseQueryWrapper);
    }

    @Override
    public CourseDto getCourseDtoById(String courseId) {

       return baseMapper.selectCourseDto(courseId);

    }

    @Override
    public void updateBuyCount(String courseId) {

        Course course = baseMapper.selectById(courseId);
        course.setBuyCount(course.getBuyCount()+1);
        baseMapper.updateById(course);
    }
}
