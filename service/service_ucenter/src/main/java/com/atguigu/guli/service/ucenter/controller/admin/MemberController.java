package com.atguigu.guli.service.ucenter.controller.admin;

import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.service.ucenter.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/ucenter/member")
@Slf4j
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("count-register-num/{day}")
    public R getCount(@PathVariable String day){

       Integer count = memberService.selectCount(day);

       return R.ok().data("registerNum",count);

    }
}
