package com.whalegoods.controller;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.DeviceModel;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.service.DeviceModelService;
import com.whalegoods.util.ReType;
import com.whalegoods.util.ShiroUtil;
import com.whalegoods.util.StringUtil;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 后台系统设备型号管理接口
 * @author henrysun
 * 2018年5月28日 上午10:10:38
 */
@Controller
@RequestMapping(value = "/model")
public class DeviceModelController {
	
	  @Autowired
	  DeviceModelService deviceModelService;

	  /**
	   * 跳转到设备型号列表页面
	   * @author henrysun
	   * 2018年4月26日 下午3:29:12
	   */
	  @GetMapping(value = "showDeviceModel")
	  @RequiresPermissions("model:show")
	  public String showDeviceModel(Model model) {
	    return "/device/model/deviceModelList";
	  }

	  /**
	   * 查询设备型号列表接口
	   * @author henrysun
	   * 2018年4月26日 下午3:29:23
	   */
	  @GetMapping(value = "showDeviceModelList")
	  @ResponseBody
	  @RequiresPermissions("model:show")
	  public ReType showDeviceModelList(Model model, DeviceModel deviceModel, String page, String limit) {
	    return deviceModelService.selectByPage(deviceModel,Integer.valueOf(page),Integer.valueOf(limit));
	  }

	  /**
	   * 跳转到添加设备型号页面
	   * @author henrysun
	   * 2018年4月26日 下午3:30:10
	   */
	  @GetMapping(value = "showAddDeviceModel")
	  public String showAddDeviceModel(Model model) {
	    return "/device/model/add-deviceModel";
	  }

	  /**
	   * 添加设备型号接口
	   * @author henrysun
	   * 2018年4月26日 下午3:30:25
	   */
	  @PostMapping(value = "addDeviceModel")
	  @ResponseBody
	  public ResBody addDeviceModel(DeviceModel deviceModel) {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		deviceModel.setId(StringUtil.getUUID());
    	String currentUserId=ShiroUtil.getCurrentUserId();
    	deviceModel.setCreateBy(currentUserId);
    	deviceModel.setUpdateBy(currentUserId);
    	deviceModelService.insert(deviceModel);
	    return resBody;
	  }

	  /**
	   * 跳转到更新设备型号页面
	   * @author henrysun
	   * 2018年4月26日 下午3:30:35
	   */
	  @GetMapping(value = "showUpdateDeviceModel")
	  public String showUpdateDeviceModel(String id, Model model){
		DeviceModel deviceModel=deviceModelService.selectById(id);
		model.addAttribute("model", deviceModel);
	    return "/device/model/update-deviceModel";
	  }


	  /**
	   * 更新设备型号接口
	   * @author henrysun
	   * 2018年4月26日 下午3:30:49
	   */
	  @PostMapping(value = "updateDeviceModel")
	  @ResponseBody
	  public ResBody updateDevice(@RequestBody DeviceModel deviceModel) {
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		  deviceModelService.updateByObjCdt(deviceModel);
		  return resBody;
	  }

	  /**
	   * 删除设备型号接口
	   * @author henrysun
	   * 2018年4月26日 下午3:32:39
	   */
	  @PostMapping(value = "/delDeviceModel")
	  @ResponseBody
	  @RequiresPermissions("model:del")
	  public ResBody delDeviceModel(@RequestParam String id) {
	    ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
	    deviceModelService.deleteById(id);
	   return resBody;
	  }

}
