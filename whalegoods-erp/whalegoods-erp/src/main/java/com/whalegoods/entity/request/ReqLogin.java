package com.whalegoods.entity.request;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 登录API请求映射实体类
 * @author henrysun
 * 2018年5月3日 下午5:07:40
 */
@Getter
@Setter
@ToString
public class ReqLogin  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="username必填")
	private String username;
	
	@NotEmpty(message="password必填")
	private String password;
}
