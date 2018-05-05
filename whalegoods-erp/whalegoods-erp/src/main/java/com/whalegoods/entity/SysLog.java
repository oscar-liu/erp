package com.whalegoods.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统日志表实体类
 * @author henrysun
 * 2018年5月4日 下午4:11:34
 */
@Getter
@Setter
@ToString
public class SysLog implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private String id;

    private String userName;

    private String ip;

    private String type;

    private String text;

    private String param;

    private Date createTime;
}