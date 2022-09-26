package com.gh.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.gh.constant.MessageConstant;
import com.gh.constant.RedisMessageConstant;
import com.gh.entity.Result;
import com.gh.pojo.Member;
import com.gh.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 处理会员相关操作
 */
@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    //电子邮箱快速登陆
    @RequestMapping("/login")
    public Result login(HttpServletResponse response,@RequestBody Map map){
        String emailAddress = (String) map.get("emailAddress");
        String validateCode = (String) map.get("validateCode");
        //从Redis中获取保存的验证码
        String validateCodeInRedis = jedisPool.getResource().get(emailAddress + RedisMessageConstant.SENDTYPE_LOGIN);
        if (validateCodeInRedis!=null&&validateCode!=null&&validateCode.equals(validateCodeInRedis)){
            //验证码输入正确
            //判断当前用户是否为会员（查询会员表来确定）
            Member member = memberService.findByEmailAddress(emailAddress);
            if (member==null){
                //不是会员，自动完成注册（自动将当前用户信息保存到会员表）
                member=new Member();
                member.setRegTime(new Date());
                member.setEmail(emailAddress);
                memberService.add(member);
            }
            //向客户端浏览器写入Cookie，内容为邮箱地址
            Cookie cookie=new Cookie("login_member_email",emailAddress);
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24*30);
            response.addCookie(cookie);
            //保存会员信息保存到Redis中
            String json = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(emailAddress,60*30,json);
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        }else {
            //验证码输入错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }
}