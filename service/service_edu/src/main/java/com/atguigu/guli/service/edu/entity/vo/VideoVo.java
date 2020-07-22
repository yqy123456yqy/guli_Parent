package com.atguigu.guli.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class VideoVo implements Serializable {

    private String id;
    private String title;
    private Integer sort;
    private boolean free;
    private String videoSourceId;

}
