package com.whalegoods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*import com.whalegoods.base.BaseMapper;
import com.whalegoods.base.impl.BaseServiceImpl;*/
import com.whalegoods.entity.SysRole;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.SysRoleMapper;
import com.whalegoods.service.RoleService;

/**
 * @author zhuxiaomeng
 * @date 2017/12/19.
 * @email 154040976@qq.com
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<SysRole,String> implements RoleService {

  @Autowired
  private SysRoleMapper roleMapper;

  @Override
  public BaseMapper<SysRole, String> getMapper() {
    return roleMapper;
  }

  @Override
  public int deleteById(String id) {
    return roleMapper.deleteById(id);
  }

  @Override
  public int insert(SysRole record) {
    return roleMapper.insert(record);
  }

  @Override
  public SysRole selectById(String id) {
    return roleMapper.selectById(id);
  }

  @Override
  public int updateByCondition(SysRole record) {
    return roleMapper.updateByCondition(record);
  }

  @Override
  public List<SysRole> selectListByPage(SysRole sysRole) {
    return roleMapper.selectListByCondition(sysRole);
  }

}
