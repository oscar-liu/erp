package com.whalegoods.exception;

/**
 * 统一错误码异常
 * <p>
 * Created by simon on 14/03/2017.
 */
public class BizServiceException extends BizException {

  public BizServiceException(Throwable cause) {
    super(cause);
  }

  public BizServiceException(Throwable cause, int code, String... params) {
    super(cause, code, params);
  }

  public BizServiceException(int code, String... params) {
    super(code, params);
  }
}
