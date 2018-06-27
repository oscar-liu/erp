package com.whalegoods.mapper;

import java.util.List;

import com.whalegoods.entity.ReportBase;
import com.whalegoods.entity.ReportByGoods;
import com.whalegoods.entity.ReportTotalCountAndAmount;


public interface ReportByGoodsMapper extends BaseMapper<ReportByGoods,String> {
	
	int insertBatch(List<ReportByGoods> list);
	
	ReportTotalCountAndAmount selectTotalSalesCountAndAmount(ReportBase objCdt);
}