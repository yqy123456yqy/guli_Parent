package com.atguigu.guli.service.ucenter.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginVo implements Serializable {

    private String mobile;
    private String password;
}
