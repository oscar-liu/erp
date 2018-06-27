package com.whalegoods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.ReportBase;
import com.whalegoods.entity.ReportByGoods;
import com.whalegoods.entity.ReportTotalCountAndAmount;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.ReportByGoodsMapper;
import com.whalegoods.service.ReportByGoodsService;

@Service
public  class ReportByGoodsServiceImpl extends BaseServiceImpl<ReportByGoods,String> implements ReportByGoodsService  {
	
	@Autowired
	private ReportByGoodsMapper reportByGoodsMapper;
	
	@Override
	public BaseMapper<ReportByGoods, String> getMapper() {		
		return reportByGoodsMapper;
	}

	@Override
	public int insertBatch(List<ReportByGoods> list) {
		return reportByGoodsMapper.insertBatch(list);
	}
	
	@Override
	public ReportTotalCountAndAmount selectTotalSalesCountAndAmount(ReportBase objCdt) {
		return reportByGoodsMapper.selectTotalSalesCountAndAmount(objCdt);
	}

}
