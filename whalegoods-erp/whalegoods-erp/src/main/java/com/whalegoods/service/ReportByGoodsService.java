package com.whalegoods.service;

import java.util.List;

import com.whalegoods.entity.ReportBase;
import com.whalegoods.entity.ReportByGoods;
import com.whalegoods.entity.ReportTotalCountAndAmount;

public interface ReportByGoodsService extends BaseService<ReportByGoods,String>{

	int insertBatch(List<ReportByGoods> list);
	
	ReportTotalCountAndAmount selectTotalSalesCountAndAmount(ReportBase objCdt);
	
}
