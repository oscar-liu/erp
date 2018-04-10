package com.whalegoods.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whalegoods.common.ResBody;
import com.whalegoods.entity.request.ReqCreateQrCode;
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
	public PayService payService;

  @PostMapping(value="/createQrCode")
  ResBody getlistGoodsAdsTop(@RequestBody ReqCreateQrCode model) {
	  logger.info("收到生成商品支付二维码接口："+model.toString());
	  Map<String,Object> mapData=payService.getPrepayMapData(model);
	  ResBody resBody=new ResBody(0,"成功");
	  resBody.setData(mapData);
	  return resBody;
	}
  
}
