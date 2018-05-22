package com.whalegoods.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.Checkbox;
import com.whalegoods.entity.SysJob;
import com.whalegoods.entity.SysJobRole;
import com.whalegoods.entity.SysRole;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.SysJobMapper;
import com.whalegoods.service.RoleService;
import com.whalegoods.service.SysJobRoleService;
import com.whalegoods.service.SysJobService;


@Service
public class SysJobServiceImpl extends BaseServiceImpl<SysJob,String> implements SysJobService {
	
	  @Autowired
	  SysJobMapper sysJobMapper;
	  
	  @Autowired
	  RoleService roleService;
	  
	  @Autowired
	  SysJobRoleService sysJobRoleService;

	  @Override
	  public BaseMapper<SysJob, String> getMapper() {
	    return sysJobMapper;
	  }
	  
	  @Override
	  public List<Checkbox> getJobRoleByJson(String id){
	    List<SysRole> roleList=roleService.selectListByObjCdt(new SysRole());
	    SysJobRole sysJobRole =new SysJobRole();
	    sysJobRole.setJobId(id);
	    List<SysJobRole>  kList= sysJobRoleService.selectListByObjCdt(sysJobRole);
	    List<Checkbox> checkboxList=new ArrayList<>();
	    Checkbox checkbox=null;
	    for(SysRole sysRole:roleList){
	      checkbox=new Checkbox();
	      checkbox.setId(sysRole.getId());
	      checkbox.setName(sysRole.getRoleName());
	      for(SysJobRole sysJobRole2 :kList){
	        if(sysJobRole2.getRoleId().equals(sysRole.getId())){
	          checkbox.setCheck(true);
	        }
	      }
	      checkboxList.add(checkbox);
	    }
	    return checkboxList;
	  }

}
