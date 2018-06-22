package com.whalegoods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.ReportByDeviceTotal;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.ReportByDeviceTotalMapper;
import com.whalegoods.service.ReportByDeviceTotalService;

@Service
public  class ReportByDeviceTotalServiceImpl extends BaseServiceImpl<ReportByDeviceTotal,String> implements ReportByDeviceTotalService  {
	
	@Autowired
	private ReportByDeviceTotalMapper reportByDeviceTotalMapper;
	
	@Override
	public BaseMapper<ReportByDeviceTotal, String> getMapper() {
		return reportByDeviceTotalMapper;
	}

	@Override
	public int insertBatch(List<ReportByDeviceTotal> list) {
		return reportByDeviceTotalMapper.insertBatch(list);
	}
}
