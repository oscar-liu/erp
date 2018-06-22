package com.whalegoods.mapper;

import java.util.List;

import com.whalegoods.entity.ReportByDeviceTotal;


public interface ReportByDeviceTotalMapper extends BaseMapper<ReportByDeviceTotal,String> {
	
	int insertBatch(List<ReportByDeviceTotal> list);
}