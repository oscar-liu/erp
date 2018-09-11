package com.whalegoods.mapper;

import java.util.List;
import java.util.Map;

import com.whalegoods.entity.ReportBase;
import com.whalegoods.entity.ReportBaseExcel;
import com.whalegoods.entity.ReportByDevice;
import com.whalegoods.entity.ReportByGoods;
import com.whalegoods.entity.ReportTotalCountAndAmount;


public interface ReportBaseMapper extends BaseMapper<ReportBase,String> {
	
	int insertBatch(List<ReportBase> list);
	
	List<ReportByDevice> selectListGroupByDevice(Map<String,Object> mapCdt);
	
	List<ReportByGoods> selectListGroupByGoods(Map<String,Object> mapCdt);
	
	ReportTotalCountAndAmount selectTotalSalesCountAndAmount(ReportBase objCdt);
	
	List<ReportBaseExcel> selectReportListByObjCdt(ReportBase objCdt);
}