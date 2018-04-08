package com.whalegoods.service;


import com.whalegoods.entity.GoodsAdsTop;
import com.whalegoods.entity.SysRoleUser;
import com.whalegoods.entity.SysUser;
import com.whalegoods.entity.response.ResGoodsAdsTop;
import com.whalegoods.util.Checkbox;
import com.whalegoods.util.JsonUtil;

import java.util.List;
import java.util.Map;

/**
 * 顶部广告Service接口层
 * @author henry-sun
 *
 */
public interface GoodsAdsTopService extends BaseService<GoodsAdsTop,String> {

  SysUser login(String username);

  @Override
  GoodsAdsTop selectByPrimaryKey(String id);

  /**
   * 分页查询
   * @param
   * @return
   */
  List<GoodsAdsTop> selectListByPage(GoodsAdsTop sysUser);

  int count();

  /**
   * 新增
   * @param user
   * @return
   */
  int add(GoodsAdsTop user);

  /**
   * 删除
   * @param id
   * @return
   */
  JsonUtil delById(String id,boolean flag);

  int checkUser(String username);


  int updateByPrimaryKey(GoodsAdsTop sysUser);

  List<SysRoleUser> selectByCondition(SysRoleUser sysRoleUser);

  public List<Checkbox> getUserRoleByJson(String id);

  /**
   * 更新密码
   * @param user
   * @return
   */
  int rePass(GoodsAdsTop user);
  
  List<ResGoodsAdsTop> selectByIdOfJpAndSupp(Map<String,Object> condition);

int deleteByPrimaryKey(String id);

}
