package com.whalegoods.service;


import com.whalegoods.common.ResBody;
import com.whalegoods.entity.Device;
import com.whalegoods.entity.SysRoleUser;
import com.whalegoods.entity.SysUser;
import com.whalegoods.exception.SystemException;
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
  
  /**
   * 更新设备信息
   * @author chencong
   * 2018年4月9日 上午11:00:39
 * @throws SystemException 
   */
  void updateDevice(Device model) throws SystemException;
  
  /**
   * 查询设备运营状态，运营状态（1正在运行 2停止运行）
   * @author chencong
   * 2018年4月9日 上午10:59:49
   */
  int getOperateStatus(Map<String,Object> condition);
  
  /**
   * 查询最新柜机APK包版本号和下载链接
   * @author chencong
   * 2018年4月9日 下午3:16:05
   */
  ResBody getApk(Map<String,Object> condition);

}
