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

import com.whalegoods.common.ResBody;
import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.request.ReqBase;
import com.whalegoods.entity.request.ReqGetInfoByPathOrGoodsCode;
import com.whalegoods.entity.request.ReqUpStock;
import com.whalegoods.entity.response.ResDeviceGoodsInfo;
import com.whalegoods.service.DeviceRoadService;

/**
 * 货道商品信息API
 * @author henry-sun
 *
 */
@RestController
@RequestMapping(value = "/v1/goods")
public class V1GoodsController  extends BaseController<Object>{

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
   * 根据当前设备商品编号或货道号获取商品信息接口
   * @author chencong
   * 2018年4月9日 下午5:08:44
   */
  @GetMapping(value="/getInfoByCode")
  ResBody getInfoByCode(@Valid ReqGetInfoByPathOrGoodsCode model) {
	  Map<String,Object> condition=new HashMap<>();
	  condition.put("deviceIdJp",model.getDevice_code_wg());
	  condition.put("deviceIdSupp",model.getDevice_code_sup());
	  condition.put("goodsOrPathCode",model.getGoods_or_path_code());
	  condition.put("type",model.getType());
	  ResDeviceGoodsInfo info=deviceRoadService.selectByPathOrGoodsCode(condition);
	  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS),info);
	  return resBody;
	}
  
  /**
   * 库存上报接口
   * @author chencong
   * 2018年4月9日 下午5:08:14
   */
  @PostMapping(value="/upStock")
  ResBody upStock(@RequestBody ReqUpStock model) {
	  Map<String,Object> condition=new HashMap<>();
	  ResBody resBody=new ResBody();
	  resBody.setResultCode(0);
	  resBody.setResultMsg("成功");
	  resBody.setData(model);
	  return resBody;
	}
  
}
