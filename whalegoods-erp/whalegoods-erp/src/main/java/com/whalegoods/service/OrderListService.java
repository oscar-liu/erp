package com.whalegoods.service;

import java.util.List;

import com.whalegoods.entity.ErpOrderList;
import com.whalegoods.entity.OrderList;
import com.whalegoods.exception.SystemException;
import com.whalegoods.util.ReType;


/**
 * 订单信息相关操作Service接口层
 * @author chencong
 *
 */
public interface OrderListService extends BaseService<OrderList,String>{

	List<ErpOrderList> selectListByObjCdt(ErpOrderList objCdt);
	
	ReType selectByPage(ErpOrderList objCdt, int page, int limit) throws SystemException;

	List<ErpOrderList> getListByObjCdt(ErpOrderList orderList) throws SystemException;

	int getCountByErpObjCdt(ErpOrderList orderList) throws SystemException;
}
