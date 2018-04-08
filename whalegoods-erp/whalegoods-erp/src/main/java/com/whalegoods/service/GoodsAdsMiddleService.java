package com.whalegoods.service;


import com.whalegoods.entity.GoodsAdsMiddle;
import com.whalegoods.entity.GoodsAdsMiddle;
import com.whalegoods.entity.SysRoleUser;
import com.whalegoods.entity.SysUser;
import com.whalegoods.entity.response.ResGoodsAdsMiddle;
import com.whalegoods.util.Checkbox;
import com.whalegoods.util.JsonUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * 中部广告Service接口层
 * @author henry-sun
 *
 */
public interface GoodsAdsMiddleService extends BaseService<GoodsAdsMiddle,String> {

  SysUser login(String username);

  @Override
  GoodsAdsMiddle selectByPrimaryKey(String id);

  /**
   * 分页查询
   * @param
   * @return
   */
  List<GoodsAdsMiddle> selectListByPage(GoodsAdsMiddle sysUser);

  int count();

  /**
   * 新增
   * @param user
   * @return
   */
  int add(GoodsAdsMiddle user);

  /**
   * 删除
   * @param id
   * @return
   */
  JsonUtil delById(String id,boolean flag);

  int checkUser(String username);


  int updateByPrimaryKey(GoodsAdsMiddle sysUser);

  List<SysRoleUser> selectByCondition(SysRoleUser sysRoleUser);

  public List<Checkbox> getUserRoleByJson(String id);

  /**
   * 更新密码
   * @param user
   * @return
   */
  int rePass(GoodsAdsMiddle user);
  
  Map<String, Object> selectByDeviceRoadId(Map<String,Object> condition) throws IllegalAccessException, InvocationTargetException;

int deleteByPrimaryKey(String id);

}
