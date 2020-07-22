package com.atguigu.guli.service.ucenter.service.impl;

import com.atguigu.guli.common.base.result.result.ResultCodeEnum;
import com.atguigu.guli.common.base.result.utils.FormUtils;
import com.atguigu.guli.common.base.result.utils.JwtInfo;
import com.atguigu.guli.common.base.result.utils.JwtUtils;
import com.atguigu.guli.common.base.result.utils.MD5;
import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.entity.vo.LoginVo;
import com.atguigu.guli.service.ucenter.entity.vo.MemberVo;
import com.atguigu.guli.service.ucenter.mapper.MemberMapper;
import com.atguigu.guli.service.ucenter.service.MemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2020-05-04
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Autowired
    private RedisTemplate redisTemplate;
    
    @Override
    public void saveRigister(MemberVo memberVo) {

        String nickname = memberVo.getNickname();
        String password = memberVo.getPassword();
        String mobile = memberVo.getMobile();
        String code = memberVo.getCode();
        //判断参数是否存在或者正确
        if(StringUtils.isEmpty(nickname) || StringUtils.isEmpty(mobile)

            || !FormUtils.isMobile(mobile) || StringUtils.isEmpty(password)
            || StringUtils.isEmpty(code)){
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }
        
        //获取redis里的验证码
        String checkCode = (String)redisTemplate.opsForValue().get(mobile);

        if(!checkCode.equals(code)){
            throw new GuliException(ResultCodeEnum.CODE_ERROR);
        }

        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("mobile", mobile);

        Integer count = baseMapper.selectCount(memberQueryWrapper);
        if(count > 0){
            throw new GuliException(ResultCodeEnum.REGISTER_MOBLE_ERROR);
        }
        Member member = new Member();

        member.setNickname(nickname);
        member.setMobile(mobile);
        member.setPassword(MD5.encrypt(password));
        member.setDisabled(false);
        member.setAvatar("https://guli-filess-1.oss-cn-shanghai.aliyuncs.com/avatar/2020/04/19/38882d1f-00a6-4c32-997f-6e43e5caa73c.jpg");


        baseMapper.insert(member);


    }

    @Override
    public String login(LoginVo loginVo) {

        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        System.out.println("mobile = " + mobile);
        System.out.println("password = " + password);
        //判断参数是否合法
        if(StringUtils.isEmpty(mobile)
        || !FormUtils.isMobile(mobile)
        || StringUtils.isEmpty(password)){
            throw  new GuliException(ResultCodeEnum.PARAM_ERROR);
        }
        //校验手机号
        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();

        memberQueryWrapper.eq("mobile", mobile);

        Member member = baseMapper.selectOne(memberQueryWrapper);
        if (member == null) {
            throw new GuliException(ResultCodeEnum.LOGIN_PHONE_ERROR);
        }
        //校验密码是否错误

        if(!MD5.encrypt(password).equals(member.getPassword())){
            throw new GuliException(ResultCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        //判断用户是否禁用
        if(member.getDisabled()){
            throw new GuliException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        JwtInfo jwtInfo = new JwtInfo(member.getId(), member.getNickname(), member.getAvatar());

        String jwtToken = JwtUtils.getJwtToken(jwtInfo, 1000);


        return jwtToken;


    }

    @Override
    public Member getByOpenid(String openId) {

        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("openid", openId);

        return baseMapper.selectOne(memberQueryWrapper);

    }

    @Override
    public MemberDto getMemberDtoByMemberId(String memberId) {

        Member member = baseMapper.selectById(memberId);
        MemberDto memberDto = new MemberDto();
        BeanUtils.copyProperties(member, memberDto);
        return memberDto;
    }

    @Override
    public Integer selectCount(String day) {

       return baseMapper.selectRegisterNumByDay(day);
    }

}
