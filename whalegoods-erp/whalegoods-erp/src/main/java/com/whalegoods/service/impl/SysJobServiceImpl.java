package com.whalegoods.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.SysJob;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.SysJobMapper;
import com.whalegoods.service.SysJobService;


@Service
public class SysJobServiceImpl extends BaseServiceImpl<SysJob,String> implements SysJobService {
	
	  @Autowired
	  SysJobMapper sysJobMapper;

	  @Override
	  public BaseMapper<SysJob, String> getMapper() {
	    return sysJobMapper;
	  }

}
