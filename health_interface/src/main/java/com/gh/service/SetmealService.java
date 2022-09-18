package com.gh.service;

import com.gh.entity.PageResult;
import com.gh.entity.QueryPageBean;
import com.gh.pojo.Setmeal;

/**
 * 体检套餐服务接口
 */
public interface SetmealService {
    public void add(Setmeal setmeal,Integer[] checkgroupIds);
    public PageResult pageQuery(QueryPageBean queryPageBean);
}
