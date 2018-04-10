package com.whalegoods.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whalegoods.common.ResBody;
import com.whalegoods.entity.request.reqCreateQrCode;

/**
 * 订单相关API
 * @author henry-sun
 *
 */
@RestController
@RequestMapping(value = "/v1/pay")
public class V1OrderController  extends BaseController<Object>{

  @PostMapping(value="/createQrCode")
  ResBody getlistGoodsAdsTop(@RequestBody reqCreateQrCode model) {
	  
	  return null;
	}
  
}
