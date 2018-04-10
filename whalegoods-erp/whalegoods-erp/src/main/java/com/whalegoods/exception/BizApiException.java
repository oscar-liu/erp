package com.whalegoods.exception;



public class BizApiException extends BizException {


	private static final long serialVersionUID = -2704857873923117602L;

  public BizApiException(Throwable cause) {
    super(cause);
  }

  public BizApiException(Throwable cause, int code, String... params) {
    super(cause, code, params);
  }

  public BizApiException(int code, String... params) {
    super(code, params);
  }
}
