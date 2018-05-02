package com.whalegoods.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.SysRoleUser;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.SysRoleUserMapper;
import com.whalegoods.service.RoleUserService;

/**
 * 系统用户角色关系业务逻辑实现类
 * @author henrysun
 * 2018年5月2日 下午1:29:23
 */
@Service
public class RoleUserServiceImpl extends BaseServiceImpl<SysRoleUser,String> implements
    RoleUserService {

  @Autowired
  private SysRoleUserMapper sysRoleUserMapper;

  @Override
  public BaseMapper<SysRoleUser, String> getMapper() {
    return sysRoleUserMapper;
  }

}
