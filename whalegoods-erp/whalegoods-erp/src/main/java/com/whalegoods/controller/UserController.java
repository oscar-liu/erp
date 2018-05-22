package com.whalegoods.controller;

import com.alibaba.fastjson.JSONObject;
import com.whalegoods.config.log.Log;
import com.whalegoods.config.log.Log.LOG_TYPE;
import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.Checkbox;
import com.whalegoods.entity.SysRoleUser;
import com.whalegoods.entity.SysUser;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.exception.BizApiException;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.RoleUserService;
import com.whalegoods.service.SysUserService;
import com.whalegoods.util.FileUtil;
import com.whalegoods.util.JsonUtil;
import com.whalegoods.util.Md5Util;
import com.whalegoods.util.ReType;
import com.whalegoods.util.ShiroUtil;
import com.whalegoods.util.StringUtil;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 系统用户相关API接口
 * @author henrysun
 * 2018年4月25日 下午7:59:49
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {
	
	  @Autowired
	  SysUserService userService;

	  @Autowired
	  RoleUserService roleUserService;
	  
	  @Autowired
	  FileUtil fileUtil;

	  /**
	   * 跳转到系统用户列表页面
	   * @author henrysun
	   * 2018年4月26日 下午3:29:12
	   */
	  @GetMapping(value = "showUser")
	  @RequiresPermissions("user:show")
	  public String showUser(Model model) {
	    return "/system/user/userList";
	  }

	  /**
	   * 查询系统用户列表接口
	   * @author henrysun
	   * 2018年4月26日 下午3:29:23
	   */
	  @GetMapping(value = "showUserList")
	  @ResponseBody
	  @RequiresPermissions("user:show")
	  public ReType showUser(Model model, SysUser user, String page, String limit) {
	    return userService.selectByPage(user,Integer.valueOf(page),Integer.valueOf(limit));
	  }

	  /**
	   * 跳转到添加系统用户页面
	   * @author henrysun
	   * 2018年4月26日 下午3:30:10
	   */
	  @GetMapping(value = "showAddUser")
	  public String addUser(Model model) {
	    List<Checkbox> checkboxList=userService.getUserRoleByJson(null);
	    model.addAttribute("boxJson",checkboxList);
	    return "/system/user/add-user";
	  }

	  /**
	   * 添加系统用户接口
	   * @author henrysun
	   * 2018年4月26日 下午3:30:25
	   */
	  @Log(desc = "添加用户")
	  @PostMapping(value = "addUser")
	  @ResponseBody
	  public JsonUtil addUser(SysUser user,String[] role) {
	    if (user == null) {
	      return JsonUtil.error("获取数据失败");
	    }
	    if (StringUtils.isBlank(user.getUserName())) {
	      return JsonUtil.error("用户名不能为空");
	    }
	    if (StringUtils.isBlank(user.getPhone())) {
		      return JsonUtil.error("手机号不能为空");
		    }
	    if (StringUtils.isBlank(user.getEmail())) {
		      return JsonUtil.error("邮箱不能为空");
		    }
	    if (StringUtils.isBlank(user.getPassword())) {
	      return JsonUtil.error("密码不能为空");
	    }
	    if(role==null){
	      return JsonUtil.error("请选择角色");
	    }
	    int result = userService.checkUser(user.getUserName());
	    if (result > 0) {
	      return JsonUtil.error("用户名已存在");
	    }
	    JsonUtil j=new JsonUtil();
	    try {
	    	user.setId(StringUtil.getUUID());
	    	String currentUserId=ShiroUtil.getCurrentUser().getId();
	    	user.setCreateBy(currentUserId);
	    	user.setUpdateBy(currentUserId);
	      userService.insert(user);
	      SysRoleUser sysRoleUser=new SysRoleUser();
	      sysRoleUser.setUserId(user.getId());
	      for(String r:role){
	    	sysRoleUser.setId(StringUtil.getUUID());
	        sysRoleUser.setRoleId(r);
	        roleUserService.insert(sysRoleUser);
	      }
	      j.setMsg("保存成功");
	    } catch (BizApiException e) {
	      j.setMsg("保存失败");
	      j.setFlag(false);
	    }
	    return j;
	  }

	  /**
	   * 跳转到更新系统用户页面
	   * @author henrysun
	   * 2018年4月26日 下午3:30:35
	   */
	  @GetMapping(value = "showUpdateUser")
	  public String showUpdateUser(String id, Model model, boolean detail) {
	    if (StringUtils.isNotEmpty(id)) {
	      //用户-角色
	     List<Checkbox> checkboxList=userService.getUserRoleByJson(id);
	      SysUser user = userService.selectById(id);
	      model.addAttribute("user", user);
	      model.addAttribute("boxJson", checkboxList);
	    }
	    model.addAttribute("detail", detail);
	    return "system/user/update-user";
	  }


	  /**
	   * 更新系统用户接口
	   * @author henrysun
	   * 2018年4月26日 下午3:30:49
	   */
	  @Log(desc = "更新用户",type = LOG_TYPE.UPDATE)
	  @PostMapping(value = "updateUser")
	  @ResponseBody
	  public JsonUtil updateUser(SysUser user,String role[]) {
	    JsonUtil jsonUtil = new JsonUtil();
	    jsonUtil.setFlag(false);
	    if (user == null) {
	      jsonUtil.setMsg("获取数据失败");
	      return jsonUtil;
	    }
	    try {
	      SysUser oldUser = userService.selectById(user.getId());
	      BeanUtils.copyProperties(user, oldUser);
	      userService.updateByObjCdt(oldUser);
	      SysRoleUser sysRoleUser =new SysRoleUser();
	      sysRoleUser.setUserId(oldUser.getId());
	      List<SysRoleUser> keyList=roleUserService.selectListByObjCdt(sysRoleUser);
	      for(SysRoleUser sysRoleUser1 :keyList){
	        roleUserService.deleteById(sysRoleUser1.getId());
	      }
	      if(role!=null){
	        for(String r:role){
	          sysRoleUser.setId(StringUtil.getUUID());
	          sysRoleUser.setRoleId(r);
	          roleUserService.insert(sysRoleUser);
	        }
	      }
	      jsonUtil.setFlag(true);
	      jsonUtil.setMsg("修改成功");
	    } catch (BizApiException e) {
	      
	    }
	    return jsonUtil;
	  }

	  /**
	   * 删除系统用户接口
	   * @author henrysun
	   * 2018年4月26日 下午3:32:39
	   */
	  @Log(desc = "删除用户",type = LOG_TYPE.DEL)
	  @PostMapping(value = "/del")
	  @ResponseBody
	  @RequiresPermissions("user:del")
	  public JsonUtil del(String id) {
	   return userService.delById(id);
	  }

	  /**
	   * 跳转到修改系统用户密码页面
	   * @author henrysun
	   * 2018年4月26日 下午3:31:08
	   */
	  @GetMapping(value = "goRePass")
	  public String goRePass(String id,Model model){
	    if(StringUtils.isEmpty(id)){
	      return "获取账户信息失败";
	    }
	    SysUser user=userService.selectById(id);
	    model.addAttribute("user",user);
	    return "/system/user/re-pass";
	  }

	  /**
	   * 修改系统用户密码页面
	   * @author henrysun
	   * 2018年4月26日 下午3:31:48
	   */
	  @Log(desc = "修改密码",type = LOG_TYPE.UPDATE)
	  @PostMapping(value = "rePass")
	  @ResponseBody
	  @RequiresPermissions("user:repass")
	  public  JsonUtil rePass(String id,String pass,String newPwd){
	    boolean flag=StringUtils.isEmpty(id)||StringUtils.isEmpty(pass)||StringUtils.isEmpty(newPwd);
	    JsonUtil j=new JsonUtil();
	    j.setFlag(false);
	    if(flag){
	      j.setMsg("获取数据失败，修改失败");
	      return j;
	    }
	    SysUser user=userService.selectById(id);
	    newPwd=Md5Util.getMd5(newPwd,user.getUserName());
	    pass=Md5Util.getMd5(pass,user.getUserName());
	    if(!pass.equals(user.getPassword())){
	        j.setMsg("密码不正确");
	        return j;
	    }
	    if(newPwd.equals(user.getPassword())){
	      j.setMsg("新密码不能与旧密码相同");

	      return j;
	    }
	    user.setPassword(newPwd);
	    try {
	      userService.rePass(user);
	      j.setMsg("修改成功");
	      j.setFlag(true);
	    }catch (BizApiException e){
	      e.printStackTrace();
	    }
	    return j;
	  }
	  
	  /**
	   * 上传头像接口
	   * @author henrysun
	   * 2018年4月26日 下午3:32:05
	 * @throws SystemException 
	   */
	  @PostMapping(value = "uploadHeadPic")
	  @ResponseBody
	  public JsonUtil uploadHeadPic(HttpServletRequest request,HttpSession session) {
		  JsonUtil jsonUtil=JsonUtil.sucess(null);
		  String childFolder="head_pic";
		  String newFileName=childFolder+"_"+System.currentTimeMillis()+StringUtil.randomString(3);
		  String fileUrl=null;
		  try {
			  fileUrl= fileUtil.uploadFile(request,childFolder,newFileName);
			  JSONObject json=new JSONObject();
			  json.put("file_url",fileUrl);
			  jsonUtil.setData(json);
		} catch (SystemException e) {
			jsonUtil.setFlag(false);
			jsonUtil.setMsg("上传失败，请联系管理员");
		}
		  return jsonUtil;
	  }

	  /**
	   * 验证用户名是否存在
	   * @author henrysun
	   * 2018年4月26日 下午3:32:17
	   */
	  @GetMapping(value = "checkUser")
	  @ResponseBody
	  public JsonUtil checkUser(@RequestParam String uname) {
	    JsonUtil j = new JsonUtil();
	    if (userService.checkUser(uname) > 0){
	      j.setFlag(false);
	      j.setMsg("用户名已存在");
	      return j;
	    }
	    j.setFlag(true);
	    return j;
	  }
	  
	  /**
	   * 更新账号状态
	   * @author henrysun
	   * 2018年5月6日 上午10:30:07
	   */
	  @PostMapping(value = "updateAccountStatus")
	  @ResponseBody
	  public ResBody updateAccountStatus(@RequestParam String id,@RequestParam(name="account_status") Boolean isCheck) {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		SysUser sysUser=new SysUser();
		sysUser.setId(id);
		if(isCheck){
			sysUser.setAccountStatus((byte) 1);
		}
		else{
			sysUser.setAccountStatus((byte) 2);
		}
		userService.updateByObjCdt(sysUser);
	    return resBody;
	  }

}
