package com.atguigu.guli.service.ucenter.service;

import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.entity.vo.LoginVo;
import com.atguigu.guli.service.ucenter.entity.vo.MemberVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author Helen
 * @since 2020-05-04
 */
public interface MemberService extends IService<Member> {

    void saveRigister(MemberVo memberVo);

    String login(LoginVo loginVo);

    Member getByOpenid(String openId);

    MemberDto getMemberDtoByMemberId(String memberId);

    Integer selectCount(String day);
}
