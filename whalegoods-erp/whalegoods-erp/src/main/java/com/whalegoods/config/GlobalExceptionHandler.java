package com.whalegoods.config;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.exception.BizException;
import com.whalegoods.exception.SystemException;

/**
 * 全局异常处理
 * @author chencong
 * 2018年4月11日 上午9:20:32
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(value = BizException.class)
  public ResBody bizeExceptionHandler(HttpServletRequest request, BizException exception) {
    logger.error(request.getContextPath()+request.getRequestURI()+" "+ConstApiResCode.getResultMsg(exception.getCode()));
    return new ResBody(exception.getCode(),ConstApiResCode.getResultMsg(exception.getCode()));
  }
  
  @ExceptionHandler(value = SystemException.class)
  public ResBody systemExceptionHandler(HttpServletRequest request, SystemException exception) {
    logger.error(request.getContextPath()+request.getRequestURI()+" "+ConstApiResCode.getResultMsg(exception.getCode()));
    return new ResBody(exception.getCode(),ConstApiResCode.getResultMsg(exception.getCode()));
  }

  @ExceptionHandler(value = BindException.class)
  public ResBody errorHandlerOverJson(HttpServletRequest request, BindException exception) {
	  StringBuilder sb=new StringBuilder();
      exception.getBindingResult().getFieldErrors().forEach((error)->{
      	sb.append(error.getDefaultMessage()).append(";");
      });
      return new ResBody(HttpStatus.SC_BAD_REQUEST, sb.toString());  
  }
  
  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public ResBody errorHandlerByJson(HttpServletRequest request, MethodArgumentNotValidException exception) {
	  StringBuilder sb=new StringBuilder();
      exception.getBindingResult().getFieldErrors().forEach((error)->{
      	sb.append(error.getDefaultMessage()).append(";");
      });
      return new ResBody(HttpStatus.SC_BAD_REQUEST, sb.toString());  
  }
}