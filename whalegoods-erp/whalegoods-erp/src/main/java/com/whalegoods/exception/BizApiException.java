package com.whalegoods.exception;


/**
 * API层异常
 * @author chencong
 * 2018年4月11日 上午9:25:30
 */
public class BizApiException extends BizException {
	
	private static final long serialVersionUID = 1L;

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
