package com.atguigu.guli.service.ucenter.controller.api;
import com.atguigu.guli.common.base.result.utils.JwtInfo;
import com.atguigu.guli.common.base.result.utils.JwtUtils;
import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.common.base.result.result.ResultCodeEnum;
import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.ucenter.entity.vo.LoginVo;
import com.atguigu.guli.service.ucenter.entity.vo.MemberVo;
import com.atguigu.guli.service.ucenter.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2020-05-04
 */
@Api(description = "会员管理")
@RestController
@RequestMapping("/api/ucenter/member")
@Slf4j
public class ApiMemberController {

    @Autowired
    MemberService memberService;

    @PostMapping("/save")
    public R rigister(@ApiParam(value = "用户信息",required = true) @RequestBody MemberVo memberVo){

        System.out.println("memberVo = " + memberVo);
        memberService.saveRigister(memberVo);

        return R.ok().message("注册成功");
    }

    @PostMapping("login")
    public R login(@ApiParam(value = "用户信息",required = true) @RequestBody LoginVo loginVo){

       String token =  memberService.login(loginVo);
       return R.ok().data("token",token).message("登陆成功");
    }

    @GetMapping("/get-login-info")
    public R getLoginInfo(HttpServletRequest request){


        try {
            JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
            System.out.println("jwtInfo = " + jwtInfo);
            return R.ok().data("userInfo",jwtInfo);
        } catch (Exception e) {

            log.error("解析用户信息失败"+ e.getMessage());
            throw  new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);

        }
    }
    @GetMapping("inner/get-member-dto/{memberId}")
    public MemberDto getMemberDto(@ApiParam(value = "会员Id",required = true)@PathVariable String memberId){

       MemberDto memberDto = memberService.getMemberDtoByMemberId(memberId);
        return memberDto;
    }

}

