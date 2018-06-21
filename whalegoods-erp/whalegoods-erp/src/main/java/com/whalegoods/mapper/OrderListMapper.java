package com.whalegoods.mapper;


import java.util.List;
import java.util.Map;

import com.whalegoods.entity.ErpOrderList;
import com.whalegoods.entity.OrderList;
import com.whalegoods.entity.ReportBase;


public interface OrderListMapper extends BaseMapper<OrderList,String> {

    List<ErpOrderList> selectListByObjCdt(ErpOrderList objCdt);

	Integer getCountByErpObjCdt(ErpOrderList orderList);
	
	String selectDeviceByOrderId(Map<String,Object> mapCdt);
	
	List<ReportBase> selectReportBaseList(Map<String,Object> mapCdt);
}