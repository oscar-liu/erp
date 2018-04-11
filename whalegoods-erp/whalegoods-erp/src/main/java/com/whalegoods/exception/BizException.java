package com.whalegoods.exception;

import com.whalegoods.constant.ConstApiResCode;

public class BizException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
   private int code = 0;
   
  private String[] params = null;
  
  private Throwable cause = null;

  protected BizException() {
  }


  public BizException(Throwable cause) {
    this(cause, ConstApiResCode.EXCEPTION_UNKNOWN);
    if (cause instanceof BizException || cause instanceof SystemException) {
      setCode(((BizException) cause).getCode());

    }
  }

  public BizException(Throwable cause, int code, String... params) {
    this.code = code;
    this.cause = cause;
    this.params = params.clone();
  }

  public BizException(int code, String... params) {
    this.code = code;
    this.params = params.clone();
  }

  public int getCode() {
    return code;
  }


  public void setCode(int code) {
    this.code = code;
  }

  @Override
  public Throwable getCause() {
    return cause;
  }

  public void setCause(Throwable cause) {
    this.cause = cause;
  }

  public String[] getParams() {
    return params;
  }

  public void setParams(String[] params) {
    this.params = params;
  }

  @Override
  public String getMessage() {
    return super.getMessage();
  }

  @Override
  public String getLocalizedMessage() {
    return super.getLocalizedMessage();
  }
}
