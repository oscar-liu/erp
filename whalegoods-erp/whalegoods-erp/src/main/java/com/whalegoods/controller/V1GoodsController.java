package com.whalegoods.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.whalegoods.common.ResBody;
import com.whalegoods.entity.response.ResDeviceGoodsInfo;
import com.whalegoods.service.DeviceRoadService;

/**
 * 货道信息控制器
 * @author henry-sun
 *
 */
@RestController
@RequestMapping(value = "/v1/goods")
public class V1GoodsController  extends BaseController<Object>{

  @Autowired
  DeviceRoadService deviceRoadService;

  @GetMapping(value="/getListByDeviceCode")
  ResBody getListByDeviceCode(@RequestParam String device_code_wg,@RequestParam String device_code_sup) {
	  Map<String,Object> condition=new HashMap<>();
	  condition.put("deviceIdJp",device_code_wg);
	  condition.put("deviceIdSupp",device_code_sup);
	  List<ResDeviceGoodsInfo> listDeviceRoad=deviceRoadService.selectByIdOfJpAndSupp(condition);
	  ResBody resBody=new ResBody();
	  resBody.setResultCode(0);
	  resBody.setResultMsg("成功");
	  resBody.setData(listDeviceRoad);
	  return resBody;
	}
  
  @GetMapping(value="/getInfoByCode")
  ResBody getInfoByCode(@RequestParam String device_code_wg,@RequestParam String device_code_sup,@RequestParam String goods_or_path_code,@RequestParam String type) {
	  Map<String,Object> condition=new HashMap<>();
	  condition.put("deviceIdJp",device_code_wg);
	  condition.put("deviceIdSupp",device_code_sup);
	  condition.put("goodsOrPathCode",goods_or_path_code);
	  condition.put("type",type);
	  ResDeviceGoodsInfo model=deviceRoadService.selectByPathOrGoodsCode(condition);
	  ResBody resBody=new ResBody();
	  resBody.setResultCode(0);
	  resBody.setResultMsg("成功");
	  resBody.setData(model);
	  return resBody;
	}
  
}
