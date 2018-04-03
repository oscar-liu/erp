package com.whalegoods.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*import com.whalegoods.base.BaseMapper;
import com.whalegoods.base.impl.BaseServiceImpl;*/
import com.whalegoods.entity.SysJob;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.SysJobMapper;
import com.whalegoods.service.JobService;

/**
 * @author zhuxiaomeng
 * @date 2018/1/6.	
 * @email 154040976@qq.com
 */
@Service
public class JobServiceImpl  extends BaseServiceImpl<SysJob,String> implements JobService {

  @Autowired
  SysJobMapper jobMapper;	
  @Override
  public BaseMapper<SysJob, String> getMapper() {
    return jobMapper;	
  }
@Override
public String show(SysJob t, int page, int limit) {
	// TODO Auto-generated method stub
	return null;
}
}
