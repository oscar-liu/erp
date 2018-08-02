package com.whalegoods.service;

import java.util.List;
import java.util.Map;

import com.whalegoods.entity.ErpOrderList;
import com.whalegoods.entity.OrderList;
import com.whalegoods.entity.ReportBase;
import com.whalegoods.exception.SystemException;
import com.whalegoods.util.ReType;


/**
 * 订单信息相关操作Service接口层
 * @author henrysun
 * 2018年6月21日 下午12:05:26
 */
public interface OrderListService extends BaseService<OrderList,String>{

	List<ErpOrderList> selectListByObjCdt(ErpOrderList objCdt);
	
	ReType selectByPage(ErpOrderList objCdt, int page, int limit) throws SystemException;

	List<ErpOrderList> getListByObjCdt(ErpOrderList orderList) throws SystemException;

	int getCountByErpObjCdt(ErpOrderList orderList) throws SystemException;
	
	String selectDeviceByOrderId(Map<String,Object> mapCdt);
	
	/**
	 * 统计昨天的数据报表
	 * @author henrysun
	 * 2018年6月21日 上午11:46:03
	 */
	List<ReportBase> selectReportBaseList(Map<String,Object> mapCdt);
	
	int updateBatch(List<OrderList> orderLists);
	
	int insertBatch(List<OrderList> orderLists);
}
