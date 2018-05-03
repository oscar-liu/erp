package com.whalegoods.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.SysRole;
import com.whalegoods.entity.SysRoleUser;
import com.whalegoods.entity.SysUser;
import com.whalegoods.exception.BizServiceException;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.SysRoleUserMapper;
import com.whalegoods.mapper.SysUserMapper;
import com.whalegoods.service.RoleService;
import com.whalegoods.service.RoleUserService;
import com.whalegoods.service.SysUserService;
import com.whalegoods.util.Checkbox;
import com.whalegoods.util.JsonUtil;
import com.whalegoods.util.Md5Util;


@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUser,String> implements SysUserService {
	
	  @Autowired
	  SysUserMapper sysUserMapper;

	  @Autowired
	  SysRoleUserMapper sysRoleUserMapper;

	  @Autowired
	  RoleService roleService;

	  @Autowired
	  RoleUserService roleUserService;
	  
	  @Override
	  public BaseMapper<SysUser, String> getMapper() {
	    return sysUserMapper;
	  }
	  
	  @Override
	  public SysUser login(String userName, String password) {
		  return sysUserMapper.login(userName, password);
	  }
	  
	  @Override
	  public int insert(SysUser record) {
	    String pwd= Md5Util.getMd5(record.getPassword().trim(),record.getUserName().trim());
	    record.setPassword(pwd);
	    return super.insert(record);
	  }

	  @Override
	  public JsonUtil delById(String id,boolean flag) {
	    if (StringUtils.isEmpty(id)) {
	      return JsonUtil.error("获取数据失败");
	    }
	    JsonUtil j=null;
	    try {
	      SysUser sysUser = selectById(id);
	      if("admin".equals(sysUser.getUserName())){
	        return JsonUtil.error("超管无法删除");
	      }
	      SysRoleUser roleUser=new SysRoleUser();
	      roleUser.setUserId(id);
	      int count=roleUserService.selectCountByObjCdt(roleUser);
	      if(count>0){
	        return JsonUtil.error("账户已经绑定角色，无法删除");
	      }
	      j=new JsonUtil();
	      if (flag) {
	        //逻辑
	        sysUser.setAccountStatus(Byte.parseByte("1"));
	        updateByObjCdt(sysUser);
	      } else {
	        //物理
	        sysUserMapper.deleteById(id);
	      }
	      j.setMsg("删除成功");
	    } catch (BizServiceException e) {
	      j.setMsg("删除失败");
	      j.setFlag(false);
	    }
	    return j;

	  }

	  @Override
	  public int checkUser(String username) {
	    return sysUserMapper.checkUser(username);
	  }

	  @Override
	  public List<Checkbox> getUserRoleByJson(String id){
	    List<SysRole> roleList=roleService.selectListByObjCdt(new SysRole());
	    SysRoleUser sysRoleUser =new SysRoleUser();
	    sysRoleUser.setUserId(id);
	    List<SysRoleUser>  kList= roleUserService.selectListByObjCdt(sysRoleUser);
	    System.out.println(kList.size());
	    List<Checkbox> checkboxList=new ArrayList<>();
	    Checkbox checkbox=null;
	    for(SysRole sysRole:roleList){
	      checkbox=new Checkbox();
	      checkbox.setId(sysRole.getId());
	      checkbox.setName(sysRole.getRoleName());
	      for(SysRoleUser sysRoleUser1 :kList){
	        if(sysRoleUser1.getRoleId().equals(sysRole.getId())){
	          checkbox.setCheck(true);
	        }
	      }
	      checkboxList.add(checkbox);
	    }
	    return checkboxList;
	  }

	  @Override
	  public int rePass(SysUser user) {
	    return sysUserMapper.rePass(user);
	  }

}
