package com.whalegoods.controller;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.whalegoods.common.ResBody;
import com.whalegoods.config.log.Log;
import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.SysUser;
import com.whalegoods.exception.SystemException;
import com.whalegoods.util.ShiroUtil;
import com.whalegoods.util.VerifyCodeUtils;

/**
 * 登录退出接口
 * @author henrysun
 * 2018年4月25日 下午7:35:28
 */
@Controller
public class LoginController {
	
	  @Autowired
	  private HttpServletRequest request;
	  
	  @Autowired
	  private HttpServletResponse response;

	  @GetMapping(value = "")
	  public String loginInit(){
	    return loginCheck();
	  }
	  
	  /**
	   * 检查是否登录，未登录跳转到登录页
	   * @return
	   */
	  @GetMapping(value = "/login")
	  public String loginCheck(){
	    Subject sub=SecurityUtils.getSubject();
	    Boolean flag=sub.isAuthenticated()||sub.isRemembered();
	    @SuppressWarnings("unused")
		Session session=sub.getSession();
	    if(flag){
	        return "/main/main";
	    }
	    return "/login";
	  }
	  
	  /**
	   * 登录表单提交接口
	   * @author henrysun
	   * 2018年4月25日 下午7:35:39
	   */
	  @PostMapping(value = "/login")
	  public String login(SysUser user,Model model){
	    String codeMsg = (String)request.getAttribute("shiroLoginFailure");
	    if("code.error".equals(codeMsg)){
	      model.addAttribute("message","验证码错误");
	      return "/login";
	    }
	    UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername().trim(),user.getPassword());
	    Subject subject = ShiroUtil.getSubject();
	    String msg=null;
	    try{
	      subject.login(token);
	      if(subject.isAuthenticated()){
	        return "/main/main";
	      }
	    }catch (UnknownAccountException e) {
	      msg = "用户名/密码错误";
	    } catch (IncorrectCredentialsException e) {
	      msg = "用户名/密码错误";
	    } catch (ExcessiveAttemptsException e) {
	      msg = "登录失败多次，账户锁定10分钟";
	    }
	    if(msg!=null){
	      model.addAttribute("message",msg);
	    }
	    return "/login";
	  }
	  
	  @GetMapping(value = "goLogin")
	  public String goLogin(Model model,ServletRequest request){
	    Subject sub=SecurityUtils.getSubject();
	    if(sub.isAuthenticated()){
	      return "/main/main";
	    }else{
	      model.addAttribute("message","请重新登录");
	      return "/login";
	    }
	  }

	  /**
	   * 用户退出登录接口
	   * @author henrysun
	   * 2018年4月25日 下午7:38:55
	   */
	  @Log(desc = "用户退出平台")
	  @GetMapping(value = "/logout")
	  public String logout(){
	    Subject sub=SecurityUtils.getSubject();
	    sub.logout();
	    return "/login";
	  }

	  /**
	   * 生成验证码接口
	   * @author henrysun
	   * 2018年4月25日 下午7:38:44
	   */
	  @GetMapping(value="/getCode")
	  public void getYzm() throws SystemException{
	    try {
	      response.setHeader("Pragma", "No-cache");
	      response.setHeader("Cache-Control", "no-cache");
	      response.setDateHeader("Expires", 0);
	      response.setContentType("image/jpg");
	      //生成随机字串
	      String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
	      //存入会话session
	      HttpSession session = request.getSession(true);
	      session.setAttribute("_code", verifyCode.toLowerCase());
	      //生成图片
	      int w = 146, h = 33;
	      VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);
	    } catch (Exception e) {
	      throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
	    }
	  }
}
