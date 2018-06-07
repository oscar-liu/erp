package com.whalegoods.service;

import java.util.List;

import com.whalegoods.entity.SysJobRole;

public interface SysJobRoleService extends BaseService<SysJobRole,String> {
	
	int deleteByJobId(String jobId);
	
    int insertBatch(List<SysJobRole> list);
}
