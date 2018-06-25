package com.whalegoods.service;

import java.util.List;

import com.whalegoods.entity.ReportByGoods;

public interface ReportByGoodsService extends BaseService<ReportByGoods,String>{

	int insertBatch(List<ReportByGoods> list);
	
}
