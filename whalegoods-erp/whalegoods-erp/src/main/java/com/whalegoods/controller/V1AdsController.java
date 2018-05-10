package com.whalegoods.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whalegoods.constant.ConstAdsMiddleTime;
import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.request.ReqBase;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.GoodsAdsMiddleService;
import com.whalegoods.service.GoodsAdsTopService;
import com.whalegoods.util.DateUtil;

/**
 * 广告促销API
 * @author henry-sun
 *
 */
@RestController
@RequestMapping(value = "/v1/ads")
public class V1AdsController {

  @Autowired
  GoodsAdsTopService goodsAdsTopService;
  
  @Autowired
  GoodsAdsMiddleService goodsAdsMiddleService;

  @GetMapping(value="/getTopList")
  ResBody getlistGoodsAdsTop(@Valid ReqBase model) {
	  Map<String,Object> mapCdt=new HashMap<>();
	  mapCdt.put("deviceIdJp",model.getDevice_code_wg());
	  mapCdt.put("deviceIdSupp",model.getDevice_code_sup());
	  List<Map<String, Object>> listMapData=goodsAdsTopService.selectAdsTopList(mapCdt);
	  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS),listMapData);
	  return resBody;
	}
  
  @GetMapping(value="/getMiddleList")
  ResBody getlistGoodsAdsMiddle(@Valid ReqBase model) throws SystemException  {
	  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
	  Map<String,Object> mapCdt=new HashMap<>();
	  mapCdt.put("deviceIdJp",model.getDevice_code_wg());
	  mapCdt.put("deviceIdSupp",model.getDevice_code_sup());
	  Date nowDate=new Date();
	  Map<String,Object> mapData=new HashMap<>();
	  if(DateUtil.belongTime(nowDate,DateUtil.getFormatHms(ConstAdsMiddleTime.ZAO_ONE,nowDate), DateUtil.getFormatHms(ConstAdsMiddleTime.ZAO_TWO,nowDate))){
		  mapCdt.put("type",ConstAdsMiddleTime.ONE);
		  mapData.put("startTime", ConstAdsMiddleTime.ZAO_ONE);
		  mapData.put("endTime", ConstAdsMiddleTime.ZAO_TWO);
	  }
	  if(DateUtil.belongTime(nowDate,DateUtil.getFormatHms(ConstAdsMiddleTime.ZHONG_ONE,nowDate), DateUtil.getFormatHms(ConstAdsMiddleTime.ZHONG_TWO,nowDate))){
		  mapCdt.put("type",ConstAdsMiddleTime.TWO);
		  mapData.put("startTime", ConstAdsMiddleTime.ZHONG_ONE);
		  mapData.put("endTime", ConstAdsMiddleTime.ZHONG_TWO);
	  }
	  if(DateUtil.belongTime(nowDate,DateUtil.getFormatHms(ConstAdsMiddleTime.WAN_ONE,nowDate), DateUtil.getFormatHms(ConstAdsMiddleTime.WAN_TWO,nowDate))){
		  mapCdt.put("type",ConstAdsMiddleTime.THREE);
		  mapData.put("startTime", ConstAdsMiddleTime.WAN_ONE);
		  mapData.put("endTime", ConstAdsMiddleTime.WAN_TWO);
	  }
	  mapData.put("data",goodsAdsMiddleService.selectAdsMiddleList(mapCdt,mapData));
	  resBody.setData(mapData);
	  return resBody;
	}
}
