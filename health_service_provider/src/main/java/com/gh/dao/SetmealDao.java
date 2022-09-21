package com.gh.dao;

import com.gh.pojo.CheckGroup;
import com.gh.pojo.Setmeal;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    public void add(Setmeal setmeal);
    public void setSetmealAndCheckGroup(Map map);
    public Page<Setmeal> findByCondition(String queryString);
    public List<Setmeal> findAll();
    public Setmeal findById4Detail(Integer id);
}
