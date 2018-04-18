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
import com.whalegoods.common.ResBody;
import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.DeviceRoad;
import com.whalegoods.entity.request.ReqBase;
import com.whalegoods.entity.request.ReqGetInfoByPathOrGoodsCode;
import com.whalegoods.entity.response.ResDeviceGoodsInfo;
import com.whalegoods.exception.BizApiException;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.DeviceRoadService;
import com.whalegoods.util.StringUtil;

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
	  if(model.getType()==1){
		  if(StringUtil.isEmpty(model.getGoods_code())){
			  throw new BizApiException(ConstApiResCode.GOODS_CODE_NOT_EMPTY);
		  }
	  }
	  if(model.getType()==2){
		  if(StringUtil.isEmpty(model.getPath_code())){
			  throw new BizApiException(ConstApiResCode.PATH_CODE_NOT_EMPTY);
		  }
	  }
	  Map<String,Object> condition=new HashMap<>();
	  condition.put("deviceIdJp",model.getDevice_code_wg());
	  condition.put("deviceIdSupp",model.getDevice_code_sup());
	  condition.put("goodsCode",model.getGoods_code());
	  condition.put("pathCode",model.getPath_code());
	  condition.put("type",model.getType());
	  ResDeviceGoodsInfo info=deviceRoadService.selectByPathOrGoodsCode(condition);
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
		  deviceRoad.setStock((Integer)map.get("stock"));
		  deviceRoadService.updateDeviceRoad(deviceRoad);
	}
	  return resBody;
	}
  
}
