package com.whalegoods.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.OrderList;
import com.whalegoods.mapper.OrderListMapper;

import com.whalegoods.service.OrderListService;
import com.whalegoods.util.DateUtil;

/**
 * 订单表业务操作实现类
 * @author chencong
 * 2018年4月18日 下午4:38:34
 */
@Service
public  class OrderListServiceImpl implements OrderListService  {
	
	@Autowired
	private OrderListMapper orderListMapper;

	@Override
	public OrderList selectByCondition(Map<String, Object> mapCdt) {
		mapCdt.put("prefix", DateUtil.getCurrentMonth());
		return orderListMapper.selectByCondition(mapCdt);
	}

	@Override
	public int updateOrderList(OrderList model) {
		model.setPrefix(DateUtil.getCurrentMonth());
		return orderListMapper.updateOrderList(model); 
	}

	@Override
	public int insertOrderList(OrderList model) {
		model.setPrefix(DateUtil.getCurrentMonth());
		return orderListMapper.insertOrderList(model);
	}

	

}
