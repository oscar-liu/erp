package com.whalegoods.mapper;

import java.util.List;

import com.whalegoods.entity.SysJobRole;

public interface SysJobRoleMapper extends BaseMapper<SysJobRole,String> {
	
	/**
	 * 根据jobId删除记录
	 * @author henrysun
	 * 2018年6月6日 下午5:21:45
	 */
	int deleteByJobId(String jobId);
	
    int insertBatch(List<SysJobRole> list);
}