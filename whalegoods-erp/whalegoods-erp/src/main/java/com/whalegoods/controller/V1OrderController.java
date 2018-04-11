package com.whalegoods.controller;

import java.util.Map;

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
import com.whalegoods.entity.request.ReqCreateQrCode;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.PayService;
import com.whalegoods.util.XmlUtil;

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
   * 微信支付结果通知回调
   * @return
   * @throws SystemException
   */
  @PostMapping(value="/wxNotify")
  ResBody wxNotify() throws SystemException {
	  Map<String,String> mapNotify=XmlUtil.parseXML(request);
	  logger.info("收到微信支付结果通知请求："+mapNotify.toString());
	  return payService.wxNofity(mapNotify);
	}
  
  /**
   * 查询交易状态
   * @param order
   * @return
   */
  @GetMapping(value="/getOrderStatus")
  ResBody getOrderStatus(@RequestParam String order)  {
	  logger.info("收到查询交易状态API请求："+order);
	  return payService.getOrderStatus(order);
	}
  
}
