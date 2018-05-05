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
	
	/**
	 * 登录
	 * @author henrysun
	 * 2018年5月3日 下午5:47:26
	 */
	SysUser login(String userName);
	
	  /**
	   * 根据主键删除系统用户
	   * @author henrysun
	   * 2018年5月3日 下午5:20:49
	   */
	  JsonUtil delById(String id,boolean flag);

	  int checkUser(String username);

	  public List<Checkbox> getUserRoleByJson(String id);

	  /**
	   * 更新密码
	   * @author henrysun
	   * 2018年5月3日 下午5:21:08
	   */
	  int rePass(SysUser user);

}
