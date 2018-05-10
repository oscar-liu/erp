package com.whalegoods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.whalegoods.entity.ErpOrderList;
import com.whalegoods.entity.OrderList;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.OrderListMapper;

import com.whalegoods.service.OrderListService;
import com.whalegoods.util.ReType;

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

	@Override
	public List<ErpOrderList> selectListByObjCdt(ErpOrderList objCdt) {
		return orderListMapper.selectListByObjCdt(objCdt);
	}

	@Override
	public ReType selectByPage(ErpOrderList objCdt, int page, int limit) {
		Page<ErpOrderList> tPage= PageHelper.startPage(page,limit);
	    List<ErpOrderList> tList=this.selectListByObjCdt(objCdt);	    
	    ReType reType=new ReType(tPage.getTotal(),tList);
		return reType;
	}

}
