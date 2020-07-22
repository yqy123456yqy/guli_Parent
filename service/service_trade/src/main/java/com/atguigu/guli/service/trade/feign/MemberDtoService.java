package com.atguigu.guli.service.trade.feign;

import com.atguigu.guli.service.base.dto.MemberDto;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-ucenter")
public interface MemberDtoService {

    @GetMapping("/api/ucenter/member/inner/get-member-dto/{memberId}")
    MemberDto getMemberDto(@ApiParam(value = "会员Id",required = true)@PathVariable String memberId);
}
