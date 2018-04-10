package com.whalegoods.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.whalegoods.common.ResBody;
import com.whalegoods.entity.response.ResGoodsAdsTop;
import com.whalegoods.service.GoodsAdsMiddleService;
import com.whalegoods.service.GoodsAdsTopService;

/**
 * 广告促销API
 * @author henry-sun
 *
 */
@RestController
@RequestMapping(value = "/v1/ads")
public class V1AdsController  extends BaseController<Object>{

  @Autowired
  GoodsAdsTopService goodsAdsTopService;
  
  @Autowired
  GoodsAdsMiddleService goodsAdsMiddleService;

  @GetMapping(value="/getTopList")
  ResBody getlistGoodsAdsTop(@RequestParam String device_code_wg,@RequestParam String device_code_sup) {
	  Map<String,Object> condition=new HashMap<>();
	  condition.put("deviceIdJp",device_code_wg);
	  condition.put("deviceIdSupp",device_code_sup);
	  List<ResGoodsAdsTop> listGoodsAdsTop=goodsAdsTopService.selectByIdOfJpAndSupp(condition);
	  ResBody resBody=new ResBody();
	  resBody.setResultCode(0);
	  resBody.setResultMsg("成功");
	  resBody.setData(listGoodsAdsTop);
	  return resBody;
	}
  
  @GetMapping(value="/getMiddleList")
  ResBody getlistGoodsAdsMiddle(@RequestParam String device_code_wg,@RequestParam String device_code_sup) throws IllegalAccessException, InvocationTargetException {
	  Map<String,Object> condition=new HashMap<>();
	  condition.put("deviceIdJp",device_code_wg);
	  condition.put("deviceIdSupp",device_code_sup);
	  Map<String,Object> resultMap=goodsAdsMiddleService.selectByDeviceId(condition);
	  ResBody resBody=new ResBody();
	  resBody.setResultCode(0);
	  resBody.setResultMsg("成功");
	  resBody.setData(resultMap);
	  return resBody;
	}
}
