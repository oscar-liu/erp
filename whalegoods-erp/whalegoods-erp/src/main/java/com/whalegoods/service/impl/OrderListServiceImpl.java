package com.whalegoods.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.OrderList;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.OrderListMapper;

import com.whalegoods.service.OrderListService;

/**
 * 订单表业务操作实现类
 * @author chencong
 * 2018年4月18日 下午4:38:34
 */
@Service
public  class OrderListServiceImpl extends BaseServiceImpl<OrderList,String> implements OrderListService  {
	
	@Autowired
	private OrderListMapper orderListMapper;
	
	@Override
	public BaseMapper<OrderList, String> getMapper() {		
		return orderListMapper;
	}

}
