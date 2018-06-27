package com.whalegoods.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.ReportBase;
import com.whalegoods.entity.ReportByDevice;
import com.whalegoods.entity.ReportByDeviceTotal;
import com.whalegoods.entity.ReportTotalCountAndAmount;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.ReportByDeviceMapper;
import com.whalegoods.service.ReportByDeviceService;

@Service
public  class ReportByDeviceServiceImpl extends BaseServiceImpl<ReportByDevice,String> implements ReportByDeviceService  {
	
	@Autowired
	private ReportByDeviceMapper reportByDeviceMapper;
	
	@Override
	public BaseMapper<ReportByDevice, String> getMapper() {		
		return reportByDeviceMapper;
	}

	@Override
	public int insertBatch(List<ReportByDevice> list) {
		return reportByDeviceMapper.insertBatch(list);
	}

	@Override
	public List<ReportByDeviceTotal> selectListGroupByOrderDay(Map<String, Object> mapCdt) {
		return reportByDeviceMapper.selectListGroupByOrderDay(mapCdt);
	}

	@Override
	public ReportTotalCountAndAmount selectTotalSalesCountAndAmount(ReportBase objCdt) {
		return reportByDeviceMapper.selectTotalSalesCountAndAmount(objCdt);
	}

}
