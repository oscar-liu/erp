package com.whalegoods.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.SysRole;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.SysRoleMapper;
import com.whalegoods.service.RoleService;

/**
 * 系统角色业务逻辑实现类
 * @author henrysun
 * 2018年5月2日 下午1:28:39
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<SysRole,String> implements RoleService {

  @Autowired
  private SysRoleMapper roleMapper;

  @Override
  public BaseMapper<SysRole, String> getMapper() {
    return roleMapper;
  }

}
