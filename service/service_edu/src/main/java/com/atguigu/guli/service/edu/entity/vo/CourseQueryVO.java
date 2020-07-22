package com.atguigu.guli.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CourseQueryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    //一级科目
    private String subjectParentId;
    //二级科目
    private String subjectId;
    //标题
    private String title;
    //讲师
    private String teacherId;
}
