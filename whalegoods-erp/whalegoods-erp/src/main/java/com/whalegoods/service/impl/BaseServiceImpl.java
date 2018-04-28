package com.whalegoods.service.impl;


import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.whalegoods.entity.response.ResPageList;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.service.BaseService;

/**
 * 通用service层{@link=BaseService}实现类
 * @author henrysun
 * 2018年4月28日 上午3:46:41
 */
public abstract class BaseServiceImpl <T,E extends Serializable> implements BaseService<T,E> {

  public abstract BaseMapper<T,E> getMapper();
  
  @Override
  public T selectById(E id) {
    return getMapper().selectById(id);
  }
  
  @Override
  public List<T> selectListByCondition(T cdt){
      return getMapper().selectListByCondition(cdt);
  }
  
  @Override
  public String  selectByPage(T t,int page,int limit){
    List<T> tList=this.selectListByCondition(t);
    Page<T> tPage= PageHelper.startPage(page,limit);
    ResPageList reType=new ResPageList(tPage.getTotal(),tList);
    return JSON.toJSONString(reType);
  }
  
  @Override
  public int insert(T model) {
    return getMapper().insert(model);
  }
  
  @Override
  public int updateByCondition(T model) {
    return getMapper().updateByCondition(model);
  }

  @Override
  public int deleteById(E id) {
    return getMapper().deleteById(id);
  }

}
