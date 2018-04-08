package com.whalegoods.service;


import com.whalegoods.entity.DeviceRoad;
import com.whalegoods.entity.SysRoleUser;
import com.whalegoods.entity.SysUser;
import com.whalegoods.entity.response.ResDeviceGoodsInfo;
import com.whalegoods.util.Checkbox;
import com.whalegoods.util.JsonUtil;

import java.util.List;
import java.util.Map;

/**
 * 货道信息Service接口层
 * @author henry-sun
 *
 */
public interface DeviceRoadService extends BaseService<DeviceRoad,String> {

  SysUser login(String username);

  @Override
  DeviceRoad selectByPrimaryKey(String id);

  /**
   * 分页查询
   * @param
   * @return
   */
  List<DeviceRoad> selectListByPage(DeviceRoad sysUser);

  int count();

  /**
   * 新增
   * @param user
   * @return
   */
  int add(DeviceRoad user);

  /**
   * 删除
   * @param id
   * @return
   */
  JsonUtil delById(String id,boolean flag);

  int checkUser(String username);


  int updateByPrimaryKey(DeviceRoad sysUser);

  List<SysRoleUser> selectByCondition(SysRoleUser sysRoleUser);

  public List<Checkbox> getUserRoleByJson(String id);

  /**
   * 更新密码
   * @param user
   * @return
   */
  int rePass(DeviceRoad user);
  
  List<ResDeviceGoodsInfo> selectByIdOfJpAndSupp(Map<String,Object> condition);
  
  ResDeviceGoodsInfo selectByPathOrGoodsCode(Map<String,Object> condition);

int deleteByPrimaryKey(String id);

}
