package com.whalegoods.service;

import com.whalegoods.entity.SysUser;
import com.whalegoods.util.Checkbox;
import com.whalegoods.util.JsonUtil;

import java.util.List;

/**
 * 系统用户相关业务逻辑接口
 * @author henrysun
 * 2018年4月28日 下午4:41:45
 */
public interface SysUserService extends BaseService<SysUser,String> {

  SysUser login(String username);

  /**
   * 新增
   * @param user
   * @return
   */
  int add(SysUser user);

  /**
   * 删除
   * @param id
   * @return
   */
  JsonUtil delById(String id,boolean flag);

  int checkUser(String username);

  public List<Checkbox> getUserRoleByJson(String id);

  /**
   * 更新密码
   * @param user
   * @return
   */
  int rePass(SysUser user);

}
