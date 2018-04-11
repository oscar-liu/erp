package com.whalegoods.service.impl;


import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.whalegoods.common.CurrentUser;
import com.whalegoods.exception.BizServiceException;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.service.BaseService;
import com.whalegoods.util.ReType;

/**
 * 通用service层{@link=BaseService}实现类
 * @author chencong
 *
 * @param <T>
 * @param <E>
 */
public abstract class BaseServiceImpl <T,E extends Serializable> implements BaseService<T,E> {

  private static Logger logger= LoggerFactory.getLogger(BaseServiceImpl.class);

  public abstract BaseMapper<T,E> getMapper();
  
  @Override
  public T selectByPrimaryKey(E id) {
    return getMapper().selectByPrimaryKey(id);
  }
  
  @Override
  public List<T> selectListByPage(T model){
      return getMapper().selectListByPage(model);
  }
  
  /**
   * 公共展示类
   * @param t 实体
   * @param page 页
   * @param limit 行
   * @return
   */
 @Override
  public String  show(T t,int page,int limit){
    List<T> tList=null;
    Page<T> tPage= PageHelper.startPage(page,limit);
    try{
      tList=getMapper().selectListByPage(t);
    }catch (BizServiceException e){
      logger.error("class:BaseServiceImpl ->method:show->message:"+e.getMessage());
      e.printStackTrace();
    }
    ReType reType=new ReType(tPage.getTotal(),tList);
    return JSON.toJSONString(reType);
  }
  
  @Override
  public int insert(T model) {
	model=addValue(model,true);
    return getMapper().insert(model);
  }

  @Override
  public int insertSelective(T model) {
    model=addValue(model,true);
    return getMapper().insertSelective(model);
  }
  
  @Override
  public int updateByPrimaryKeySelective(T model) {
    model=addValue(model,false);
    return getMapper().updateByPrimaryKeySelective(model);
  }
  
  @Override
  public int updateByPrimaryKey(T model) {
    model=addValue(model,false);
    return getMapper().updateByPrimaryKey(model);
  }

  @Override
  public int deleteByPrimaryKey(E id) {
    return getMapper().deleteByPrimaryKey(id);
  }
  
  /**
   * 公共字段注入创建更新实体信息 可通过super调用
   * @param model 实体类
   * @param flag true添加  false更新
   * @return 更新后的实体类
   */
  protected T  addValue(T model,boolean flag){
    CurrentUser currentUser= (CurrentUser) SecurityUtils.getSubject().getSession().getAttribute("curentUser");
    //统一处理公共字段
    Class<?> clazz=model.getClass();
    try {
      if(flag){
        Field field=clazz.getDeclaredField("createBy");
        field.setAccessible(true);
        field.set(model,currentUser.getId());
        Field fieldDate=clazz.getDeclaredField("createDate");
        fieldDate.setAccessible(true);
        fieldDate.set(model,new Date());
      }else{
        Field field=clazz.getDeclaredField("updateBy");
        field.setAccessible(true);
        field.set(model,currentUser.getId());
        Field fieldDate=clazz.getDeclaredField("updateDate");
        fieldDate.setAccessible(true);
        fieldDate.set(model,new Date());
      }
    } catch (NoSuchFieldException e) {
    	logger.error("公共字段注入创建更新实体信息报错："+e.getMessage());
    } catch (IllegalAccessException e) {
    	logger.error("公共字段注入创建更新实体信息报错："+e.getMessage());
    }
    return model;
  }

}
