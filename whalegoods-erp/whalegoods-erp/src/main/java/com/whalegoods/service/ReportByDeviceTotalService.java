package com.whalegoods.service;

import java.util.List;

import com.whalegoods.entity.ReportByDeviceTotal;

public interface ReportByDeviceTotalService extends BaseService<ReportByDeviceTotal,String>{

	int insertBatch(List<ReportByDeviceTotal> list);
	
}
