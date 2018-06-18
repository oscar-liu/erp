package com.whalegoods.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.SysRoleMenu;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.SysRoleMenuMapper;
import com.whalegoods.service.RoleMenuService;

/**
 * 角色菜单关系业务逻辑实现类
 * @author henrysun
 * 2018年5月2日 上午11:53:11
 */
@Service
public class RoleMenuServiceImpl extends BaseServiceImpl<SysRoleMenu,String> implements  RoleMenuService {
	
    @Autowired
    private SysRoleMenuMapper roleMenuMapper;
    
    @Override
    public BaseMapper<SysRoleMenu, String> getMapper() {
        return roleMenuMapper;
    }

}
