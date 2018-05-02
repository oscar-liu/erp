package com.whalegoods.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.request.ReqBase;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.entity.response.ResGoodsAdsTop;
import com.whalegoods.exception.SystemException;
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
	  Map<String,Object> mapCdt=new HashMap<>();
	  mapCdt.put("deviceIdJp",model.getDevice_code_wg());
	  mapCdt.put("deviceIdSupp",model.getDevice_code_sup());
	  List<ResGoodsAdsTop> listGoodsAdsTop=goodsAdsTopService.selectAdsTopList(mapCdt);
	  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS),listGoodsAdsTop);
	  return resBody;
	}
  
  @GetMapping(value="/getMiddleList")
  ResBody getlistGoodsAdsMiddle(@Valid ReqBase model) throws SystemException  {
	  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
	  Map<String,Object> mapCdt=new HashMap<>();
	  mapCdt.put("deviceIdJp",model.getDevice_code_wg());
	  mapCdt.put("deviceIdSupp",model.getDevice_code_sup());
	  Map<String,Object> resultMap=goodsAdsMiddleService.selectAdsMiddleList(mapCdt);
	  resBody.setData(resultMap);
	  return resBody;
	}
}
