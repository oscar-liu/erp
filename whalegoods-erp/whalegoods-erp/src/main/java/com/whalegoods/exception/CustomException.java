package com.whalegoods.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.whalegoods.exception.MyException;

/**
 * 全局异常处理
 * @author chencong
 * 2018年3月30日 下午4:53:44
 */
public class CustomException implements HandlerExceptionResolver {

  @Override
  public ModelAndView resolveException(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse, Object o, Exception e) {
    ModelAndView mv=null;
    //如果是shiro未授权异常
    if(e instanceof UnauthorizedException){
      mv=new ModelAndView("/error/error");
      mv.setViewName("/login");
    }
    else {
    	MyException myExecption=null;
 	    if(e instanceof MyException){
  	      myExecption=(MyException)e;
  	    }else{
  	      myExecption=new MyException("未知错误");
  	    }
  	    //错误信息
  	    String message=myExecption.getMessage();
  	    mv=new ModelAndView();
  	    //将错误信息传到页面
  	    mv.addObject("message",message);  
	}
    return mv;
  }
}
