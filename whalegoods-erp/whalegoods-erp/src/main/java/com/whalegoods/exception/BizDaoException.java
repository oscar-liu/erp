package com.whalegoods.exception;


/**
 * DAO层异常
 * @author chencong
 * 2018年4月11日 上午9:25:59
 */
public class BizDaoException extends BizException {

	private static final long serialVersionUID = 1L;

	public BizDaoException(Throwable cause) {
		super(cause);
	}

	public BizDaoException(Throwable cause, int code, String... params) {
		super(cause, code, params);
	}

	public BizDaoException(int code, String... params) {
		super(code, params);
	}
}
