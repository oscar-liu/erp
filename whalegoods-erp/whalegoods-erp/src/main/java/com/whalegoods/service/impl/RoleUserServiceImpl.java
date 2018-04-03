package com.whalegoods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*import com.whalegoods.base.BaseMapper;
import com.whalegoods.base.impl.BaseServiceImpl;*/
import com.whalegoods.entity.SysRoleUser;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.SysRoleUserMapper;
import com.whalegoods.service.RoleUserService;

/**
 * @author zhuxiaomeng
 * @date 2017/12/21.
 * @email 154040976@qq.com
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

  @Override
  public int deleteByPrimaryKey(SysRoleUser sysRoleUser) {
    return sysRoleUserMapper.deleteByPrimaryKey(sysRoleUser);
  }

  @Override
  public int selectCountByCondition(SysRoleUser sysRoleUser) {
    return sysRoleUserMapper.selectCountByCondition(sysRoleUser);
  }

  @Override
  public List<SysRoleUser> selectByCondition(SysRoleUser sysRoleUser) {
    return sysRoleUserMapper.selectByCondition(sysRoleUser);
  }

@Override
public String show(SysRoleUser t, int page, int limit) {
	// TODO Auto-generated method stub
	return null;
}
}
