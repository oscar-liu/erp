package com.whalegoods.service;

import java.util.List;

import com.whalegoods.entity.Checkbox;
import com.whalegoods.entity.SysJob;

public interface SysJobService extends BaseService<SysJob,String> {

	List<Checkbox> getJobRoleByJson(String id);
	

}
