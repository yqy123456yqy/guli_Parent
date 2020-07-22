package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.vo.TeacherQueryVo;
import com.atguigu.guli.service.edu.mapper.CourseMapper;
import com.atguigu.guli.service.edu.mapper.TeacherMapper;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.atguigu.guli.service.edu.feign.feiginTest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2020-04-03
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {


    @Autowired
    feiginTest feigintest;

    @Autowired
    CourseMapper courseMapper;

    @Override
    public Page<Teacher> selectPage(Page<Teacher> pageParam, TeacherQueryVo teacherQueryVo) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByAsc("sort");

        if(teacherQueryVo == null){
            return baseMapper.selectPage(pageParam, queryWrapper);
        }

        String name = teacherQueryVo.getName();
        Integer level = teacherQueryVo.getLevel();
        String joinDateBegin = teacherQueryVo.getJoinDateBegin();
        String joinDateEnd = teacherQueryVo.getJoinDateEnd();

        if(!StringUtils.isEmpty(name)){
            queryWrapper.likeRight("name", name);
        }
        if(level != null){
            queryWrapper.eq("level", level);
        }
        if(!StringUtils.isEmpty(joinDateBegin)){
            queryWrapper.ge("join_date", joinDateBegin);
        }
        if(!StringUtils.isEmpty(joinDateEnd)){
            queryWrapper.le("join_date", joinDateEnd);
        }

        return baseMapper.selectPage(pageParam, queryWrapper);

    }

    @Override
    public List<Map<String, Object>> selectNameList(String key) {

        QueryWrapper<Teacher> queryWrapper = new QueryWrapper();
        queryWrapper.select("name");
        queryWrapper.likeRight("name", key);

        List<Map<String,Object>> list = baseMapper.selectMaps(queryWrapper);

        return list;
    }

    @Override
    public boolean removeAvatarById(String id) {

        Teacher teacher = baseMapper.selectById(id);

        if(teacher != null){
            String avatar = teacher.getAvatar();
            if(!StringUtils.isEmpty(avatar)){
                R r = feigintest.deleteFile(avatar);
                return r.getSuccess();
            }
        }

        return false;
    }

    @Override
    public Map<String, Object> get(String id) {

        Teacher teacher = baseMapper.selectById(id);

        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.eq("teacher_id", id);
        List<Course> courses = courseMapper.selectList(courseQueryWrapper);

        Map<String,Object> map = new HashMap<>();
        map.put("teacher",teacher);
        map.put("courses",courses);
        return map;


    }

    @Cacheable(value = "index",key = "'selectTeacher'")
    @Override
    public List<Teacher> selectTeacher() {
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.orderByDesc("sort");
        teacherQueryWrapper.last("limit 4");
      return baseMapper.selectList(teacherQueryWrapper);


    }


}
