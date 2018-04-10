package com.whalegoods.exception;



public class BizDaoException extends BizException {

	private static final long serialVersionUID = -2400481044433310559L;

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
