package com.whalegoods.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.ReportBase;
import com.whalegoods.entity.ReportBaseExcel;
import com.whalegoods.entity.ReportByDevice;
import com.whalegoods.entity.ReportByGoods;
import com.whalegoods.entity.ReportTotalCountAndAmount;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.ReportBaseMapper;
import com.whalegoods.service.ReportBaseService;

@Service
public  class ReportBaseServiceImpl extends BaseServiceImpl<ReportBase,String> implements ReportBaseService  {
	
	@Autowired
	private ReportBaseMapper reportBaseMapper;
	
	@Override
	public BaseMapper<ReportBase, String> getMapper() {		
		return reportBaseMapper;
	}

	@Override
	public int insertBatch(List<ReportBase> list) {
		return reportBaseMapper.insertBatch(list);
	}

	@Override
	public List<ReportByDevice> selectListGroupByDevice(Map<String, Object> mapCdt) {
		return reportBaseMapper.selectListGroupByDevice(mapCdt);
	}

	@Override
	public List<ReportByGoods> selectListGroupByGoods(Map<String, Object> mapCdt) {
		return reportBaseMapper.selectListGroupByGoods(mapCdt);
	}

	@Override
	public ReportTotalCountAndAmount selectTotalSalesCountAndAmount(ReportBase objCdt) {
		return reportBaseMapper.selectTotalSalesCountAndAmount(objCdt);
	}

	@Override
	public List<ReportBaseExcel> selectReportListByObjCdt(ReportBase objCdt) {
		return reportBaseMapper.selectReportListByObjCdt(objCdt);
	}
	
}
