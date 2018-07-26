package com.whalegoods.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.constant.ConstOrderStatus;
import com.whalegoods.constant.ConstSysParamName;
import com.whalegoods.entity.ErpOrderList;
import com.whalegoods.entity.OrderList;
import com.whalegoods.entity.request.ReqCloseOrder;
import com.whalegoods.entity.request.ReqCreatePrepay;
import com.whalegoods.entity.request.ReqCreateQrCode;
import com.whalegoods.entity.request.ReqRefund;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.exception.BizApiException;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.OrderListService;
import com.whalegoods.service.PayService;
import com.whalegoods.util.DateUtil;

/**
 * 订单相关API
 * @author henrysun
 * 2018年6月5日 下午7:15:24
 */
@RestController
@RequestMapping(value = "/v1/pay")
public class V1OrderController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	public PayService payService;
	
	@Autowired
	public OrderListService orderListService;
	
	/**
	 * 生成预支付订单
	 * @author henrysun
	 * 2018年6月5日 下午7:15:08
	 */
  @PostMapping(value="/createPrepay")
  ResBody createPrepay(@RequestBody @Valid ReqCreatePrepay model) throws SystemException {
	  logger.info("收到createPrepay请求：{}",model.toString());
	  //如果是促销商品，则下单时viewtime字段不能为空
	  if(model.getSaleType()==2){
		  if(model.getViewTime()==null){
			  logger.error("viewtime字段为空");
			  throw new BizApiException(ConstApiResCode.VIEWTIME_NOT_EMPTY);
		  }
	  }
	  ResBody resBody=payService.createPrepay(model,(byte) 1);
	  logger.info("结果：{}",resBody.toString());
	  return resBody;
	}

	/**
	 * 生成支付二维码
	 * @author henrysun
	 * 2018年6月5日 下午7:15:04
	 */
  @PostMapping(value="/createQrCode")
  ResBody createQrCode(@RequestBody @Valid ReqCreateQrCode model) throws SystemException {
	  logger.info("收到createQrCode请求：{}",model.toString());
	  ResBody resBody=payService.getQrCode(model);
	  logger.info("结果：{}",resBody.toString());
	  return resBody;
	}
  
  /**
   * 查询交易状态
   * @param order
   * @return
 * @throws SystemException 
   */
  @GetMapping(value="/getOrderStatus")
  ResBody getOrderStatus(@RequestParam String order) throws SystemException  {
	  logger.info("收到getOrderStatus请求："+order);
	  ResBody resBody=payService.getOrderStatus(order);
	  logger.info("结果：{}",resBody.toString());
	  return resBody;
	}
  
  /**
   * 柜机退款申请
   * @param order
   * @return
 * @throws SystemException 
   */
  @PostMapping(value="/refund")
  ResBody refund(@RequestBody @Valid ReqRefund model) throws SystemException  {
	  logger.info("收到refund请求：{}",model.toString());
	  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
	  //异步执行
	  payService.refund(model);
	  payService.refundNotify(model);
	  return resBody;
	}
  
  /**
   * ERP后台退款申请
   * @param order
   * @return
 * @throws SystemException 
   */
  @PostMapping(value="/erpRefund")
  ResBody erpRefund(@RequestBody @Valid ReqRefund model) throws SystemException  {
	  return payService.refund(model);
	}
  
  /**
   * 关闭订单
   * @author henrysun
   * 2018年7月25日 下午6:44:04
   */
  @PostMapping(value="/closeOrder")
  ResBody closeOrder(@RequestBody @Valid ReqCloseOrder model) throws SystemException  {
	  logger.info("收到closeOrder请求：{}",model.toString());
	  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
	  ErpOrderList objCdt=new ErpOrderList();
	  objCdt.setPrefix(Integer.valueOf(DateUtil.formatDateTime(DateUtil.timestampToDate(System.currentTimeMillis())).substring(0,7).replace(ConstSysParamName.GANG,"")));
	  objCdt.setOrderId(model.getOrder());
	  objCdt.setOrderStatus(ConstOrderStatus.NOT_PAY);
	  if(payService.closeOrder(objCdt).getResultCode()==ConstApiResCode.SUCCESS){
		  OrderList objCdt2=new OrderList();
		  objCdt2.setOrderId(model.getOrder());
		  objCdt2.setPrefix(objCdt.getPrefix().toString());
		  objCdt2.setOrderStatus(ConstOrderStatus.CLOSED);
		  orderListService.updateByObjCdt(objCdt2);
	  }
	  return resBody;
	}
  
}
