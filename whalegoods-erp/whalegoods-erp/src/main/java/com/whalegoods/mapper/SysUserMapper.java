package com.whalegoods.mapper;

import com.whalegoods.entity.SysUser;


public interface SysUserMapper extends BaseMapper<SysUser, String>{

	/**
	 * 登录
	 * @author henrysun
	 * 2018年5月3日 下午5:50:10
	 */
    SysUser login(String userName);

    int checkUser(String username);

    /**
     * 更新密码
     * @author henrysun
     * 2018年5月3日 下午5:17:45
     */
    int rePass(SysUser user);

    /**
     * 根据任务名称查找对应的收件人邮箱列表
     * @author henrysun
     * 2018年6月6日 上午11:54:56
     */
    String[] getEmailArr(String jobName);

}