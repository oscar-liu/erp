package com.whalegoods.controller;

import com.alibaba.fastjson.JSON;
import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.DeviceModel;
import com.whalegoods.entity.DeviceModelStandard;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.service.DeviceModelService;
import com.whalegoods.service.DeviceModelStandardService;
import com.whalegoods.util.ReType;
import com.whalegoods.util.ShiroUtil;
import com.whalegoods.util.StringUtil;

import java.util.List;

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
 * 后台系统型号标准管理接口
 * @author henrysun
 * 2018年4月25日 下午7:59:49
 */
@Controller
@RequestMapping(value = "/standard")
public class DeviceModelStandardController {
	
	  @Autowired
	  DeviceModelStandardService standardService;
	  
	  @Autowired
	  DeviceModelService modelService;

	  /**
	   * 跳转到型号标准列表页面
	   * @author henrysun
	   * 2018年4月26日 下午3:29:12
	   */
	  @GetMapping(value = "showDeviceModelStandard")
	  @RequiresPermissions("standard:show")
	  public String showDeviceModelStandard(Model model) {
	    return "/device/standard/deviceModelStandardList";
	  }

	  /**
	   * 查询型号标准列表接口
	   * @author henrysun
	   * 2018年4月26日 下午3:29:23
	   */
	  @GetMapping(value = "showDeviceModelStandardList")
	  @ResponseBody
	  @RequiresPermissions("standard:show")
	  public ReType showDeviceModelStandardList(Model model, DeviceModelStandard standard, String page, String limit) {
	    return standardService.selectByPage(standard,Integer.valueOf(page),Integer.valueOf(limit));
	  }

	  /**
	   * 跳转到设置型号标准页面
	   * @author henrysun
	   * 2018年4月26日 下午3:30:10
	   */
	  @GetMapping(value = "showSetDeviceModelStandard")
	  public String showSetDeviceModelStandard(Model model) {
		model.addAttribute("modelList",modelService.selectListByObjCdt(new DeviceModel()));
	    return "/device/standard/set-deviceModelStandard";
	  }

	  /**
	   * 设置型号标准接口
	   * @author henrysun
	   * 2018年4月26日 下午3:30:25
	   */
	  @PostMapping(value = "setDeviceModelStandard")
	  @ResponseBody
	  public ResBody setDeviceModelStandard(@RequestBody String standard) {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		List<DeviceModelStandard> list = JSON.parseArray(standard, DeviceModelStandard.class);
		if(list.size()>0){
			standardService.deleteByDeviceModelId(list.get(0).getDeviceModelId());
			for (DeviceModelStandard deviceModelStandard : list) {
				deviceModelStandard.setId(StringUtil.getUUID());
				deviceModelStandard.setCreateBy(ShiroUtil.getCurrentUserId());
				deviceModelStandard.setUpdateBy(ShiroUtil.getCurrentUserId());
				standardService.insert(deviceModelStandard); 
			}
		}
	    return resBody;
	  }


}
