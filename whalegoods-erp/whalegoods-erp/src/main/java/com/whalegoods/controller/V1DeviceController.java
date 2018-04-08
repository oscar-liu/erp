package com.whalegoods.controller;



import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.javassist.compiler.ast.NewExpr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whalegoods.common.ResBody;
import com.whalegoods.entity.request.reqUpDeviceStatus;
import com.whalegoods.service.DeviceService;

/**
 * 设备管理控制器
 * @author henry-sun
 *
 */
@RestController
@RequestMapping(value = "/v1/device")
public class V1DeviceController  extends BaseController<Object>{

  @Autowired
  DeviceService deviceService;

  @PostMapping(value="/updateDeviceStatus")
  ResBody updateDeviceStatus(@RequestBody reqUpDeviceStatus model) {
	  Map<String,Object> condition=new HashMap<>();
	  condition.put("deviceIdJp",model.getDeviceCodeWg());
	  condition.put("deviceIdSupp",model.getDeviceCodeSup());
	  deviceService.updateDeviceStatus(condition);
	  ResBody resBody=new ResBody();
	  resBody.setResultCode(0);
	  resBody.setResultMsg("成功");
	  return resBody;
	}
  
  
}
