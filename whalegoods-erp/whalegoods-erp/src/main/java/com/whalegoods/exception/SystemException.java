package com.whalegoods.exception;

import com.whalegoods.constant.ConstApiResCode;

import lombok.Getter;
import lombok.Setter;

/**
 * 系统异常
 * @author chencong
 * 2018年4月10日 下午6:32:27
 */
@Getter
@Setter
public class SystemException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private Integer code;

	public SystemException(Integer code) {
    this.code = code;
  }

  public String getLocalizedMessage() {
    return ConstApiResCode.getZhMsg(code);
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
