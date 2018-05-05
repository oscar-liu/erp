package com.whalegoods.controller;

import com.whalegoods.config.log.Log;
import com.whalegoods.config.log.Log.LOG_TYPE;
import com.whalegoods.entity.SysRoleUser;
import com.whalegoods.entity.SysUser;
import com.whalegoods.exception.BizApiException;
import com.whalegoods.service.RoleUserService;
import com.whalegoods.service.SysUserService;
import com.whalegoods.util.Checkbox;
import com.whalegoods.util.JsonUtil;
import com.whalegoods.util.Md5Util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
	  public String showUser(Model model, SysUser user, String page, String limit) {
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
	      userService.insert(user);
	      SysRoleUser sysRoleUser=new SysRoleUser();
	      sysRoleUser.setUserId(user.getId());
	      for(String r:role){
	        sysRoleUser.setRoleId(r);
	        roleUserService.insert(sysRoleUser);
	      }
	      j.setMsg("保存成功");
	    } catch (BizApiException e) {
	      j.setMsg("保存失败");
	      j.setFlag(false);
	      e.printStackTrace();
	    }
	    return j;
	  }

	  /**
	   * 跳转到更新系统用户页面
	   * @author henrysun
	   * 2018年4月26日 下午3:30:35
	   */
	  @GetMapping(value = "updateUser")
	  public String updateUser(String id, Model model, boolean detail) {
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
	  public JsonUtil del(String id, boolean flag) {
	   return userService.delById(id,flag);
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
	   */
	  @PostMapping(value = "upload")
	  @ResponseBody
	  public JsonUtil imgUpload(HttpServletRequest req, @RequestParam("file") MultipartFile file,
	      ModelMap model) {
	    JsonUtil j = new JsonUtil();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    SimpleDateFormat sdf1 = new SimpleDateFormat("hhmmss");

	    String fileName = sdf1.format(new Date()) + file.getOriginalFilename();
	    String objPath =
	        req.getSession().getServletContext().getRealPath("image/") + sdf.format(new Date())
	            .toString();
	    File targetFile1 = new File(objPath, fileName);
	    File file2 = new File(objPath);
	    if (!file2.exists()) {
	      file2.mkdirs();
	    }
	    if (!targetFile1.exists()) {
	      targetFile1.mkdirs();
	    }

	    try {
	      file.transferTo(targetFile1);
	    } catch (Exception e) {
	      j.setFlag(false);
	      j.setMsg("上传失败");
	      e.printStackTrace();
	    }
	    j.setMsg("image/" + sdf.format(new Date()).toString() + "/" + req.getContextPath() + fileName);
	    return j;
	  }

	  /**
	   * 验证用户名是否存在
	   * @author henrysun
	   * 2018年4月26日 下午3:32:17
	   */
	  @GetMapping(value = "checkUser")
	  @ResponseBody
	  public JsonUtil checkUser(String uname, HttpServletRequest req) {
	    JsonUtil j = new JsonUtil();
	    j.setFlag(Boolean.FALSE);
	    if (StringUtils.isEmpty(uname)) {
	      j.setMsg("获取数据失败");
	      return j;
	    }
	    int result = userService.checkUser(uname);
	    if (result > 0) {
	      j.setMsg("用户名已存在");
	      return j;
	    }
	    j.setFlag(true);
	    return j;
	  }

}
