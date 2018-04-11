package com.whalegoods.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.whalegoods.common.ResBody;
import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.request.ReqBase;
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
  ResBody getlistGoodsAdsTop(@Valid ReqBase model) {
	  Map<String,Object> condition=new HashMap<>();
	  condition.put("deviceIdJp",model.getDevice_code_wg());
	  condition.put("deviceIdSupp",model.getDevice_code_sup());
	  List<ResGoodsAdsTop> listGoodsAdsTop=goodsAdsTopService.selectByIdOfJpAndSupp(condition);
	  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS),listGoodsAdsTop);
	  return resBody;
	}
  
  @GetMapping(value="/getMiddleList")
  ResBody getlistGoodsAdsMiddle(@RequestBody @Valid ReqBase model) throws IllegalAccessException, InvocationTargetException {
	  Map<String,Object> condition=new HashMap<>();
	  condition.put("deviceIdJp",model.getDevice_code_wg());
	  condition.put("deviceIdSupp",model.getDevice_code_sup());
	  Map<String,Object> resultMap=goodsAdsMiddleService.selectByDeviceId(condition);
	  ResBody resBody=new ResBody();
	  resBody.setResultCode(0);
	  resBody.setResultMsg("成功");
	  resBody.setData(resultMap);
	  return resBody;
	}
}
