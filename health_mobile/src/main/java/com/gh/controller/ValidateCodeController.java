package com.gh.controller;

import com.gh.constant.MessageConstant;
import com.gh.constant.RedisMessageConstant;
import com.gh.entity.Result;
import com.gh.utils.MailUtils;
import com.gh.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * 验证码操作
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;

    //用户在线体检预约发送验证码
    @RequestMapping("/send4Order")
    public Result sendOrder(String emailAddress){
        //随机生成一个4位数字验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        //给用户发送验证码
        try {
            MailUtils.sendMail(MailUtils.VALIDATE_CODE,emailAddress,validateCode.toString());
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将验证码保存到redis（5分钟）
        jedisPool.getResource().setex(emailAddress+ RedisMessageConstant.SENDTYPE_ORDER,300,validateCode.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    //用户电子邮箱快速登陆发送验证码
    @RequestMapping("/send4Login")
    public Result send4Login(String emailAddress){
        //随机生成一个6位数字验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        //给用户发送验证码
        try {
            MailUtils.sendMail(MailUtils.VALIDATE_CODE,emailAddress,validateCode.toString());
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将验证码保存到redis（5分钟）
        jedisPool.getResource().setex(emailAddress+ RedisMessageConstant.SENDTYPE_LOGIN,300,validateCode.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
