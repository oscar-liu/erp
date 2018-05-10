package com.whalegoods.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.DeviceRoad;
import com.whalegoods.entity.request.ReqBase;
import com.whalegoods.entity.request.ReqGetInfoByGoodsCode;
import com.whalegoods.entity.request.ReqGetInfoByPathCode;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.entity.response.ResDeviceGoodsInfo;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.DeviceRoadService;

/**
 * 货道商品信息API
 * @author henry-sun
 *
 */
@RestController
@RequestMapping(value = "/v1/goods")
public class V1GoodsController {

  @Autowired
  DeviceRoadService deviceRoadService;
  
  @Autowired
  HttpServletRequest request;

  /**
   * 根据设备编号获取所有商品信息列表接口
   * @author chencong
   * 2018年4月9日 下午5:08:30
   */
  @GetMapping(value="/getListByDeviceCode")
  ResBody getListByDeviceCode( @Valid ReqBase model) {
	  Map<String,Object> condition=new HashMap<>();
	  condition.put("deviceIdJp",model.getDevice_code_wg());
	  condition.put("deviceIdSupp",model.getDevice_code_sup());
	  List<ResDeviceGoodsInfo> listDeviceRoad=deviceRoadService.selectByIdOfJpAndSupp(condition);
	  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS),listDeviceRoad);
	  return resBody;
	}
  
  /**
   * 根据当前设备商品编号获取商品信息接口
   * @author chencong
   * 2018年4月9日 下午5:08:44
   */
  @GetMapping(value="/getInfoByGoodsCode")
  ResBody getInfoByCode(@Valid ReqGetInfoByGoodsCode model) {
	  Map<String,Object> mapCdt=new HashMap<>();
	  mapCdt.put("deviceIdJp",model.getDevice_code_wg());
	  mapCdt.put("deviceIdSupp",model.getDevice_code_sup());
	  mapCdt.put("goodsCode",model.getGoods_code());
	  ResDeviceGoodsInfo info=deviceRoadService.selectByGoodsOrPathCode(mapCdt);
	  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS),info);
	  return resBody;
	}
  
  /**
   * 根据当前设备货道号获取商品信息接口
   * @author chencong
   * 2018年4月9日 下午5:08:44
   */
  @GetMapping(value="/getInfoByPathCode")
  ResBody getInfoByPathCode(@Valid ReqGetInfoByPathCode model) {
	  Map<String,Object> mapCdt=new HashMap<>();
	  mapCdt.put("deviceIdJp",model.getDevice_code_wg());
	  mapCdt.put("deviceIdSupp",model.getDevice_code_sup());
	  mapCdt.put("pathCode",model.getPath_code());
	  mapCdt.put("floor",model.getFloor());
	  mapCdt.put("ctn",model.getCtn());
	  ResDeviceGoodsInfo info=deviceRoadService.selectByGoodsOrPathCode(mapCdt);
	  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS),info);
	  return resBody;
	}
  
  /**
   * 库存上报接口
   * @author chencong
   * 2018年4月9日 下午5:08:14
 * @throws SystemException 
   */
  @SuppressWarnings("rawtypes")
  @PostMapping(value="/upStock")
  ResBody upStock(@RequestBody String json) throws SystemException {
	  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
	  JSONObject jsonObject=JSONObject.parseObject(json);	  
	  List<Map> mapList=JSON.parseArray(jsonObject.getString("data"),Map.class);
	  for (Map map : mapList) {
		//生成要更新的对象
		  DeviceRoad deviceRoad=new DeviceRoad();
		  deviceRoad.setDeviceIdJp(jsonObject.getString("device_code_wg")); 
		  deviceRoad.setDeviceIdSupp(jsonObject.getString("device_code_sup"));
		  deviceRoad.setPathCode(Byte.valueOf( map.get("path_code").toString()));
		  deviceRoad.setFloor(Byte.valueOf( map.get("floor").toString()));
		  deviceRoad.setCtn(Byte.valueOf( map.get("ctn").toString()));
		  deviceRoad.setStock((byte)map.get("stock"));
		  deviceRoadService.updateByObjCdt(deviceRoad);
	}
	  return resBody;
	}
  
}
