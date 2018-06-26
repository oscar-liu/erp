package com.whalegoods.mapper;

import java.util.List;
import java.util.Map;

import com.whalegoods.entity.ReportBase;
import com.whalegoods.entity.ReportByDevice;
import com.whalegoods.entity.ReportByGoods;


public interface ReportBaseMapper extends BaseMapper<ReportBase,String> {
	
	int insertBatch(List<ReportBase> list);
	
	List<ReportByDevice> selectListGroupByDevice(Map<String,Object> mapCdt);
	
	List<ReportByGoods> selectListGroupByGoods(Map<String,Object> mapCdt);
	
	ReportBase selectTotalSalesCountAndAmount(ReportBase objCdt);
}