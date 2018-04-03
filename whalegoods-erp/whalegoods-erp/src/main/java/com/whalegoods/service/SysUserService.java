package com.whalegoods.service;


/*import com.whalegoods.base.BaseService;*/
import com.whalegoods.entity.SysRoleUser;
import com.whalegoods.entity.SysUser;
import com.whalegoods.util.Checkbox;
import com.whalegoods.util.JsonUtil;

import java.util.List;

/**
 * @author zhuxiaomeng
 * @date 2017/12/4.
 * @email 154040976@qq.com
 */
public interface SysUserService extends BaseService<SysUser,String> {

  SysUser login(String username);

  @Override
  SysUser selectByPrimaryKey(String id);

  /**
   * 分页查询
   * @param
   * @return
   */
  List<SysUser> selectListByPage(SysUser sysUser);

  int count();

  /**
   * 新增
   * @param user
   * @return
   */
  int add(SysUser user);

  /**
   * 删除
   * @param id
   * @return
   */
  JsonUtil delById(String id,boolean flag);

  int checkUser(String username);


  int updateByPrimaryKey(SysUser sysUser);

  List<SysRoleUser> selectByCondition(SysRoleUser sysRoleUser);

  public List<Checkbox> getUserRoleByJson(String id);

  /**
   * 更新密码
   * @param user
   * @return
   */
  int rePass(SysUser user);

}
