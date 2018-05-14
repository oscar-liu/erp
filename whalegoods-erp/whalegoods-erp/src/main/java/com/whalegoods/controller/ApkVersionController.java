package com.whalegoods.controller;

import com.alibaba.fastjson.JSONObject;
import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.ApkVersion;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.exception.BizApiException;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.ApkVersionService;
import com.whalegoods.util.FileUtil;
import com.whalegoods.util.ReType;
import com.whalegoods.util.ShiroUtil;
import com.whalegoods.util.StringUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
 * 柜机APK相关API接口
 * @author henrysun
 * 2018年4月25日 下午7:59:49
 */
@Controller
@RequestMapping(value = "/apk")
public class ApkVersionController {
	  
	  @Autowired
	  FileUtil fileUtil;
	  
	  @Autowired
	  ApkVersionService apkVersionService;

	  /**
	   * 跳转到APK列表页面
	   * @author henrysun
	   * 2018年4月26日 下午3:29:12
	   */
	  @GetMapping(value = "showApk")
	  @RequiresPermissions("apk:show")
	  public String showApk(Model model) {
	    return "/system/apk/apkList";
	  }

	  /**
	   * 查询APK列表接口
	   * @author henrysun
	   * 2018年4月26日 下午3:29:23
	   */
	  @GetMapping(value = "showApkList")
	  @ResponseBody
	  @RequiresPermissions("apk:show")
	  public ReType showApkList(Model model, ApkVersion apkVersion, String page, String limit) {
	    return apkVersionService.selectByPage(apkVersion,Integer.valueOf(page),Integer.valueOf(limit));
	  }

	  /**
	   * 跳转到添加APK页面
	   * @author henrysun
	   * 2018年4月26日 下午3:30:10
	   */
	  @GetMapping(value = "showAddApk")
	  public String showAddApk(Model model) {
	    return "/system/apk/add-apk";
	  }

	  /**
	   * 添加APK接口
	   * @author henrysun
	   * 2018年4月26日 下午3:30:25
	   */
	  @PostMapping(value = "addApk")
	  @ResponseBody
	  public ResBody addApk(@RequestBody ApkVersion apkVersion) {
		if (apkVersionService.checkApkVersion(apkVersion.getApkVersion()) > 0){
			throw new BizApiException(ConstApiResCode.APK_VERSION_EXIST);
		}
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
	    apkVersion.setId(StringUtil.getUUID());
    	String currentUserId=ShiroUtil.getCurrentUser().getId();
    	apkVersion.setCreateBy(currentUserId);
    	apkVersion.setUpdateBy(currentUserId);
    	apkVersionService.insert(apkVersion);
	    return resBody;
	  }

	  /**
	   * 跳转到更新APK页面
	   * @author henrysun
	   * 2018年4月26日 下午3:30:35
	   */
	  @GetMapping(value = "updateApk")
	  public String updateApk(@RequestParam String id, Model model) {
		ApkVersion apkVersion = apkVersionService.selectById(id);
	    model.addAttribute("apk", apkVersion);
	    return "system/apk/update-apk";
	  }


	  /**
	   * 更新APK接口
	   * @author henrysun
	   * 2018年4月26日 下午3:30:49
	   */
	  @PostMapping(value = "updateApk")
	  @ResponseBody
	  public ResBody updateApk(@RequestBody ApkVersion apkVersion) {
		if (apkVersionService.checkApkVersion(apkVersion.getApkVersion()) > 0){
		    throw new BizApiException(ConstApiResCode.APK_VERSION_EXIST);
		}
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		apkVersionService.updateByObjCdt(apkVersion);
	    return resBody;
	  }

	  /**
	   * 删除APK接口
	   * @author henrysun
	   * 2018年4月26日 下午3:32:39
	   */
	  @PostMapping(value = "/delApk")
	  @ResponseBody
	  @RequiresPermissions("apk:del")
	  public ResBody delApk(String id) {
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		  apkVersionService.deleteById(id);
		  return resBody;
	  }
	  
	  /**
	   * 上传APK接口
	   * @author henrysun
	   * 2018年4月26日 下午3:32:05
	 * @throws SystemException 
	   */
	  @PostMapping(value = "uploadApk")
	  @ResponseBody
	  public ResBody uploadApk(HttpServletRequest request,HttpSession session) {
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		  String childFolder="apk";
		  String newFileName=childFolder+"_"+System.currentTimeMillis()+StringUtil.randomString(3);
		  String fileUrl=null;
		  try {
			  fileUrl= fileUtil.uploadFile(request,childFolder,newFileName);
			  JSONObject json=new JSONObject();
			  json.put("file_url",fileUrl);
			  resBody.setData(json);
		} catch (SystemException e) {
			resBody.setResultCode(ConstApiResCode.SYSTEM_ERROR);
			resBody.setResultMsg("上传失败，请联系管理员");
		}
		  return resBody;
	  }

	  /**
	   * 验证用户名是否存在
	   * @author henrysun
	   * 2018年4月26日 下午3:32:17
	   */
	  @GetMapping(value = "checkApkVersion")
	  @ResponseBody
	  public ResBody checkApkVersion(@RequestParam String apkVersion) {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
	    if (apkVersionService.checkApkVersion(apkVersion) > 0){
	    	throw new BizApiException(ConstApiResCode.APK_VERSION_EXIST);
	    }
	    return resBody;
	  }
	  
	  /**
	   * 更新APK状态
	   * @author henrysun
	   * 2018年5月6日 上午10:30:07
	   */
	  @PostMapping(value = "updateApkStatus")
	  @ResponseBody
	  public ResBody updateApkStatus(@RequestParam String id,@RequestParam(name="apk_status") Boolean isCheck) {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		ApkVersion apkVersion=new ApkVersion();
		apkVersion.setId(id);
		if(isCheck){
			apkVersion.setApkStatus((byte) 1);
		}
		else{
			apkVersion.setApkStatus((byte) 2);
		}
		apkVersionService.updateByObjCdt(apkVersion);
	    return resBody;
	  }

}
