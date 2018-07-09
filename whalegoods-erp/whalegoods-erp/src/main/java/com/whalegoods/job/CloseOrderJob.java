package com.whalegoods.job;

import java.util.ArrayList;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.constant.ConstOrderStatus;
import com.whalegoods.constant.ConstSysParamName;
import com.whalegoods.constant.ConstSysParamValue;
import com.whalegoods.entity.ErpOrderList;
import com.whalegoods.entity.OrderList;
import com.whalegoods.entity.request.ReqRefund;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.OrderListService;
import com.whalegoods.service.PayService;
import com.whalegoods.util.DateUtil;

/**
 * 半个小时执行一次关闭订单任务
 * @author henrysun
 * 2018年6月3日 下午9:08:21
 */
public class CloseOrderJob implements BaseJob{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PayService payService;
	
	@Autowired
	private OrderListService orderListService;
	
    public CloseOrderJob() {}
  
    public void execute(JobExecutionContext context) {
    	String startTime=null,endTime=null;
    	try {
    		endTime =DateUtil.formatDateTime(DateUtil.timestampToDate(System.currentTimeMillis()-ConstSysParamValue.BACK_TEN_MIN));
    		startTime =DateUtil.formatDateTime(DateUtil.timestampToDate(System.currentTimeMillis()-ConstSysParamValue.BACK_FOURTY_MIN));
		} catch (SystemException e) {
			logger.info("CloseOrderJob日期转换错误：{}",e.getMessage());
		}
		//查询未支付的订单
		ErpOrderList objCdt=new ErpOrderList();
		objCdt.setPrefix(Integer.valueOf(endTime.substring(0,7).replace(ConstSysParamName.GANG,"")));
		objCdt.setStartOrderTime(startTime);
		objCdt.setEndOrderTime(endTime);
		objCdt.setOrderStatus(ConstOrderStatus.NOT_PAY);
		List<ErpOrderList> listOrder=orderListService.selectListByObjCdt(objCdt);
		logger.info("查询到的未支付订单数：{}",listOrder.size());
		//先关闭订单，如果关闭失败，则直接退款
		if(listOrder.size()>0){
			List<OrderList> listOrder2=new ArrayList<>();
			for (ErpOrderList erpOrderList : listOrder) {
				try {
					OrderList orderList=new OrderList();
					orderList.setOrderId(erpOrderList.getOrderId());
					ResBody resBody=payService.closeOrder(erpOrderList);
					if(resBody.getResultCode()==ConstApiResCode.SUCCESS){
						orderList.setOrderStatus(ConstOrderStatus.CLOSED);
					}
					else{
						ReqRefund refund=new ReqRefund();
						refund.setOrder(erpOrderList.getOrderId());
						if(payService.refund(refund).getResultCode()==ConstApiResCode.SUCCESS){
							orderList.setOrderStatus(ConstOrderStatus.APPLY_REFUND_SUCCESS);	
						}
					}
					orderList.setPrefix(objCdt.getPrefix().toString());
					listOrder2.add(orderList);
				} catch (SystemException e) {
					logger.info("关闭订单系统错误：{}",e.getMessage());
				}
			}
			orderListService.updateBatch(listOrder2);
		}
    }
}
