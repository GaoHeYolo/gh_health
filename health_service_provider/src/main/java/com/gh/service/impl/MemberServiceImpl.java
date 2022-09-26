package com.gh.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gh.dao.MemberDao;
import com.gh.pojo.Member;
import com.gh.service.MemberService;
import com.gh.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 会员服务
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByEmailAddress(String emailAddress) {
        return memberDao.findByEmailAddress(emailAddress);
    }

    //保存会员信息
    @Override
    public void add(Member member) {
        String password = member.getPassword();
        if (password!=null){
            //使用MD5将明文密码进行加密
            password = MD5Utils.md5(password);
            member.setPassword(password);

        }
        memberDao.add(member);
    }
}
