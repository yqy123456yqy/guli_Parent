package com.atguigu.guli.service.ucenter.controller.api;
import com.atguigu.guli.common.base.result.utils.ExceptionUtils;
import com.atguigu.guli.common.base.result.utils.JwtInfo;
import com.atguigu.guli.common.base.result.utils.JwtUtils;
import com.atguigu.guli.common.base.result.result.ResultCodeEnum;
import com.atguigu.guli.common.base.result.utils.HttpClientUtils;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.service.MemberService;
import com.atguigu.guli.service.ucenter.util.UcenterProperties;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/api/ucenter/wx")
@Slf4j
public class ApiWxController {

    @Autowired
    UcenterProperties ucenterProperties;


    @Autowired
    MemberService memberService;


    @GetMapping("/login")
    public String genQrConnect(HttpSession session) {

        //https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect

        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        String redirecturi = "";

        try {
            redirecturi = URLEncoder.encode(ucenterProperties.getRedirectUri(), "UTF-8");
        } catch (UnsupportedEncodingException e) {

            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.URL_ENCODE_ERROR);
        }

        String state = UUID.randomUUID().toString();

        log.info("生成state" + state);

        session.setAttribute("wx_open_state", state);

        String qrcodeUrl = String.format(baseUrl, ucenterProperties.getAppId(), redirecturi, state);
        return "redirect:" + qrcodeUrl;
    }

    @GetMapping("/callback")
    public String callback(String code, String state, HttpSession session) {

        if (StringUtils.isEmpty(code)
                || StringUtils.isEmpty(state)) {
            log.error("非法回调请求");
            throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        String sessionState = (String) session.getAttribute("wx_open_state");

        if (!sessionState.equals(state)) {
            log.error("非法回调请求");
            throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        //grant_type=authorization_code
        String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
        Map<String, String> accessTokenParam = new HashMap<>();

        accessTokenParam.put("appid", ucenterProperties.getAppId());
        accessTokenParam.put("secret", ucenterProperties.getAppSecret());
        accessTokenParam.put("code", code);
        accessTokenParam.put("grant_type", "authorization_code");

        HttpClientUtils client = new HttpClientUtils(accessTokenUrl, accessTokenParam);
        String result = "";
        try {
            client.get();
            result = client.getContent();
            System.out.println("result = " + result);
        } catch (Exception e) {
            log.error("URL编码失败" + ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.URL_ENCODE_ERROR);
        }

        Gson gson = new Gson();
        HashMap<String, Object> map = gson.fromJson(result, HashMap.class);
        Object errcodeObj = map.get("errcode");
        if (errcodeObj != null) {
            String errmsg = (String) map.get("errmsg");
            Integer errcode = (Integer) errcodeObj;
            log.error("获取access_token失败"+"errcode:"+errcode+",errmsg:"+errmsg);
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        //https://api.weixin.qq.com/sns/auth?access_token=ACCESS_TOKEN&openid=OPENID
        String access_token = (String)map.get("access_token");
        String openid = (String)map.get("openid");

        Member member = memberService.getByOpenid(openid);

        //&openid=OPENID
        if(member == null) {

            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo";
            Map<String, String> baseUserInfoParam = new HashMap<>();
            baseUserInfoParam.put("access_token", access_token);
            baseUserInfoParam.put("openid", openid);

            client = new HttpClientUtils(baseUserInfoUrl, baseUserInfoParam);

            String resultUserInfo = "";

            try {
                client.get();
                resultUserInfo = client.getContent();
            } catch (Exception e) {
                log.error("获取用户信息失败" + ExceptionUtils.getMessage(e));
                throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }


            HashMap hashMap = gson.fromJson(resultUserInfo, HashMap.class);
            Object errcode = hashMap.get("errcode");
            if (errcode != null) {
                log.error("获取用户信息失败" + "，message：" + hashMap.get("errmsg"));
                throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }

            String nickname = (String) hashMap.get("nickname");
            System.out.println("nickname = " + nickname);
            Double sex = (Double) hashMap.get("sex");
            String headimgurl = (String) hashMap.get("headimgurl");

            member = new Member();
            member.setAvatar(headimgurl);
            member.setNickname(nickname);
            member.setOpenid(openid);
            member.setSex(sex.intValue());
            memberService.save(member);
        }
            JwtInfo jwtInfo = new JwtInfo();
            jwtInfo.setAvatar(member.getAvatar());
            jwtInfo.setNickname(member.getNickname());
            jwtInfo.setId(member.getId());

            String jwtToken = JwtUtils.getJwtToken(jwtInfo, 1800);




        return "redirect:http://localhost:3000?token="+jwtToken;

    }

}
