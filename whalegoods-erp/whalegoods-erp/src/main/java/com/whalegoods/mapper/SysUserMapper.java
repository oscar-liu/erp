package com.whalegoods.mapper;

import com.whalegoods.entity.SysUser;

/**
 * 系统用户相关业务逻辑接口
 * @author henrysun
 * 2018年4月28日 上午3:18:08
 */
public interface SysUserMapper extends BaseMapper<SysUser, String>{

	/**
	 * 登录
	 * @param userName 用户名
	 * @author henrysun
	 * 2018年4月28日 上午3:18:34
	 */
    SysUser login(String userName);

    int checkUser(String username);

    /**
     * 更新密码
     * @param user
     * @return
     */
    int rePass(SysUser user);


}