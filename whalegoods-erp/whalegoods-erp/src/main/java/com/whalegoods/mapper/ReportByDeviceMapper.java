package com.whalegoods.mapper;

import java.util.List;
import java.util.Map;

import com.whalegoods.entity.ReportBase;
import com.whalegoods.entity.ReportByDevice;
import com.whalegoods.entity.ReportByDeviceTotal;
import com.whalegoods.entity.ReportTotalCountAndAmount;


public interface ReportByDeviceMapper extends BaseMapper<ReportByDevice,String> {
	
	int insertBatch(List<ReportByDevice> list);
	
	List<ReportByDeviceTotal> selectListGroupByOrderDay(Map<String,Object> mapCdt);
	
	ReportTotalCountAndAmount selectTotalSalesCountAndAmount(ReportBase objCdt);
}