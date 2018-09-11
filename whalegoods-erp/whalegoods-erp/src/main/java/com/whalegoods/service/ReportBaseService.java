package com.whalegoods.service;

import java.util.List;
import java.util.Map;

import com.whalegoods.entity.ReportBase;
import com.whalegoods.entity.ReportBaseExcel;
import com.whalegoods.entity.ReportByDevice;
import com.whalegoods.entity.ReportByGoods;
import com.whalegoods.entity.ReportTotalCountAndAmount;


/**
 * 统计基本报表相关操作Service接口层
 * @author henrysun
 * 2018年6月21日 下午12:05:21
 */
public interface ReportBaseService extends BaseService<ReportBase,String>{

	int insertBatch(List<ReportBase> list);
	
	//按照设备分组查询基本统计报表
	List<ReportByDevice> selectListGroupByDevice(Map<String,Object> mapCdt);
	
	//按照商品分组查询基本统计报表
	List<ReportByGoods> selectListGroupByGoods(Map<String,Object> mapCdt);
	
	ReportTotalCountAndAmount selectTotalSalesCountAndAmount(ReportBase objCdt);
	
	//明细统计报表
	List<ReportBaseExcel> selectReportListByObjCdt(ReportBase objCdt);
	
}
