package com.atguigu.guli.service.trade.feign.fallback;

import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.trade.feign.MemberDtoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MemberDtoServiceImpl implements MemberDtoService {
    @Override
    public MemberDto getMemberDto(String memberId) {
      log.info("熔断保护");
        return null;
    }
}
