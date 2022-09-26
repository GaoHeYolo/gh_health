package com.gh.service;

import com.gh.pojo.Member;

public interface MemberService {
    //根据手机号查询会员
    public Member findByEmailAddress(String emailAddress);
    public void add(Member member);
}
