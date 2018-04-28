package com.whalegoods.service;

/*import com.whalegoods.base.BaseService;*/
import com.whalegoods.entity.SysRole;

import java.util.List;

/**
 * @author zhuxiaomeng
 * @date 2017/12/19.
 * @email 154040976@qq.com
 */
public interface RoleService extends BaseService<SysRole,String> {

  @Override
  int deleteById(String id);

  @Override
  int insert(SysRole record);

  @Override
  SysRole selectById(String id);

  @Override
  int updateByCondition(SysRole record);

  List<SysRole> selectListByPage(SysRole sysRole);
}
