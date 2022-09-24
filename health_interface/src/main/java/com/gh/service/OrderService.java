package com.gh.service;

import com.gh.entity.Result;

import java.util.Map;

public interface OrderService{
    public Result order(Map map) throws Exception;
    public Map findById(Integer id) throws Exception;
}
