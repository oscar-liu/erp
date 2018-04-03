package com.whalegoods.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;


/**
 * 控制器基类
 * @author chencong
 *
 * @param <T> 实体类
 */
public abstract class BaseController<T> {

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.registerCustomEditor(Date.class, new CustomDateEditor(
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
    binder.registerCustomEditor(Date.class, new CustomDateEditor(
        new SimpleDateFormat("yyyy-MM-dd"), true));
  }

  @ExceptionHandler({ UnauthorizedException.class, AuthorizationException.class })
  public String authorizationException(HttpServletRequest request, HttpServletResponse response) {	  
	  if (!isAjaxRequest(request)){
		  String message="权限不足";
		  try {
			message = URLEncoder.encode(message,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	      return "redirect:/error/403?message="+message;
	    }
	  return null;
  }
  
  /**
   * 判断是否是Ajax请求
   * @param request
   * @return
   */
  private static boolean isAjaxRequest(HttpServletRequest request) {
    String requestedWith = request.getHeader("x-requested-with");
    if (requestedWith != null && requestedWith.equalsIgnoreCase("XMLHttpRequest"))
      return true;
    return false;
  }

}
