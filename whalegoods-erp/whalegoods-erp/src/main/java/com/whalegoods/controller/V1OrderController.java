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

import com.whalegoods.common.ResBody;
import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.request.ReqCreatePrepay;
import com.whalegoods.entity.request.ReqCreateQrCode;
import com.whalegoods.entity.request.ReqRefund;
import com.whalegoods.exception.BizApiException;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.PayService;

/**
 * 订单相关API
 * @author henry-sun
 *
 */
@RestController
@RequestMapping(value = "/v1/pay")
public class V1OrderController  extends BaseController<Object>{
	
	private static Logger logger = LoggerFactory.getLogger(V1OrderController.class);
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	public PayService payService;
	
	/**
	 * 生成预支付订单
	 * @param model
	 * @return
	 * @throws SystemException
	 */
  @PostMapping(value="/createPrepay")
  ResBody createPrepay(@RequestBody @Valid ReqCreatePrepay model) throws SystemException {
	  logger.info("收到生成商品支付二维码API请求："+model.toString());
	  if(model.getSaleType()==2){
		  if(model.getViewTime()==null){
			  throw new BizApiException(ConstApiResCode.VIEWTIME_NOT_EMPTY);
		  }
	  }
	  return payService.createPrepay(model);
	}

	/**
	 * 预支付生成二维码
	 * @param model
	 * @return
	 * @throws SystemException
	 */
  @PostMapping(value="/createQrCode")
  ResBody getlistGoodsAdsTop(@RequestBody @Valid ReqCreateQrCode model) throws SystemException {
	  logger.info("收到生成商品支付二维码API请求："+model.toString());
	  return payService.getQrCode(model);
	}
  
  /**
   * 查询交易状态
   * @param order
   * @return
 * @throws SystemException 
   */
  @GetMapping(value="/getOrderStatus")
  ResBody getOrderStatus(@RequestParam String order) throws SystemException  {
	  logger.info("收到查询交易状态API请求："+order);
	  return payService.getOrderStatus(order);
	}
  
  /**
   * 退款申请
   * @param order
   * @return
 * @throws SystemException 
   */
  @PostMapping(value="/refund")
  ResBody refund(@RequestBody @Valid ReqRefund model) throws SystemException  {
	  logger.info("收到退款申请API请求："+model.toString());
	  return payService.refund(model);
	}
  
}
