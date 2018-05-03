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
	 * @author henrysun
	 * 2018年5月3日 下午5:50:10
	 */
    SysUser login(String userName,String password);

    int checkUser(String username);

    /**
     * 更新密码
     * @author henrysun
     * 2018年5月3日 下午5:17:45
     */
    int rePass(SysUser user);


}