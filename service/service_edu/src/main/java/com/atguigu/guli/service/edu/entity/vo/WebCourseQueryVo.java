package com.atguigu.guli.service.edu.entity.vo;

import lombok.Data;

@Data
public class WebCourseQueryVo {

    private String subjectParentId;
    private String subjectId;
    private String buyCountSort;
    private String gmtCreateSort;
    private String priceSort;
    private Integer type;
}
