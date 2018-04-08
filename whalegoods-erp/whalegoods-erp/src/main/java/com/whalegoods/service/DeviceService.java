package com.whalegoods.service;


import com.whalegoods.entity.Device;
import com.whalegoods.entity.SysRoleUser;
import com.whalegoods.entity.SysUser;
import com.whalegoods.util.Checkbox;
import com.whalegoods.util.JsonUtil;

import java.util.List;
import java.util.Map;


public interface DeviceService extends BaseService<Device,String> {

  SysUser login(String username);

  @Override
  Device selectByPrimaryKey(String id);

  /**
   * 分页查询
   * @param
   * @return
   */
  List<Device> selectListByPage(Device sysUser);

  int count();

  /**
   * 新增
   * @param user
   * @return
   */
  int add(Device user);

  /**
   * 删除
   * @param id
   * @return
   */
  JsonUtil delById(String id,boolean flag);

  int checkUser(String username);


  int updateByPrimaryKey(Device sysUser);

  List<SysRoleUser> selectByCondition(SysRoleUser sysRoleUser);

  public List<Checkbox> getUserRoleByJson(String id);

  /**
   * 更新密码
   * @param user
   * @return
   */
  int rePass(Device user);
  
  int deleteByPrimaryKey(String id);
  
  int updateDeviceStatus(Map<String,Object> condition);

}
