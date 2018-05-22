package com.whalegoods.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.SysJobRole;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.SysJobRoleMapper;
import com.whalegoods.service.SysJobRoleService;


@Service
public class SysJobRoleServiceImpl extends BaseServiceImpl<SysJobRole,String> implements SysJobRoleService {
	
	  @Autowired
	  SysJobRoleMapper sysJobRoleMapper;

	  @Override
	  public BaseMapper<SysJobRole, String> getMapper() {
	    return sysJobRoleMapper;
	  }

}
