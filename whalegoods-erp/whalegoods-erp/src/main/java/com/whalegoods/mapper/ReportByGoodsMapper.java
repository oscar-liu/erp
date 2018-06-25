package com.whalegoods.mapper;

import java.util.List;

import com.whalegoods.entity.ReportByGoods;


public interface ReportByGoodsMapper extends BaseMapper<ReportByGoods,String> {
	
	int insertBatch(List<ReportByGoods> list);
}