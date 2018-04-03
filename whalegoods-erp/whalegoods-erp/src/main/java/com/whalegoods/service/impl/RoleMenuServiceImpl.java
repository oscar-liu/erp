package com.whalegoods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*import com.whalegoods.base.BaseMapper;
import com.whalegoods.base.impl.BaseServiceImpl;*/
import com.whalegoods.entity.SysRoleMenu;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.SysRoleMenuMapper;
import com.whalegoods.service.RoleMenuService;

/**
 * @author zhuxiaomeng
 * @date 2017/12/28.
 * @email 154040976@qq.com
 */
@Service
public class RoleMenuServiceImpl extends BaseServiceImpl<SysRoleMenu,String> implements
    RoleMenuService {
    @Autowired
    private SysRoleMenuMapper roleMenuMapper;
    @Override
    public BaseMapper<SysRoleMenu, String> getMapper() {
        return roleMenuMapper;
    }

    @Override
    public List<SysRoleMenu> selectByCondition(SysRoleMenu sysRoleMenu) {
        return roleMenuMapper.selectByCondition(sysRoleMenu);
    }

    @Override
    public int selectCountByCondition(SysRoleMenu sysRoleMenu) {
        return roleMenuMapper.selectCountByCondition(sysRoleMenu);
    }

    @Override
    public int deleteByPrimaryKey(SysRoleMenu sysRoleMenu) {
        return roleMenuMapper.deleteByPrimaryKey(sysRoleMenu);
    }

	@Override
	public String show(SysRoleMenu t, int page, int limit) {
		// TODO Auto-generated method stub
		return null;
	}
}
