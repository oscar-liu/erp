package com.whalegoods.exception;

/**
 * Service层异常
 * @author chencong
 * 2018年4月11日 上午9:24:23
 */
public class BizServiceException extends BizException {

	private static final long serialVersionUID = 1L;
	
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
