package com.whalegoods.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统用户实体类
 * @author henrysun
 * 2018年4月28日 上午3:13:27
 */
@Getter
@Setter
@ToString
public class SysUser extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String userName;

    private String password;

    private String phone;
    
    private String email;
    
    private String headPicUrl;

    private Byte accountStatus;
    
    private String role[];
    
}