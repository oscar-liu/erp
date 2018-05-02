package com.whalegoods.exception;

import com.whalegoods.constant.ConstApiResCode;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统异常，非运行时
 * @author henrysun
 * 2018年5月2日 上午11:39:40
 */
@Getter
@Setter
@ToString
public class SystemException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private Integer code;

	public SystemException(Integer code) {
    this.code = code;
	}
	
	  public String getLocalizedMessage() {
		    return ConstApiResCode.getResultMsg(code);
		  }

		  public SystemException(Integer code, String msg) {
		    super(msg);
		    this.code = code;
		  }

		  public SystemException(Integer code, String msg, Throwable cause) {
		    super(msg, cause);
		    this.code = code;
		  }

}
