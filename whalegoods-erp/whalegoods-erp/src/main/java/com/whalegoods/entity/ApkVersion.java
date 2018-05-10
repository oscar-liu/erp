package com.whalegoods.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 柜机APK包实体类
 * @author henrysun
 * 2018年4月28日 上午3:13:27
 */
@Getter
@Setter
@ToString
public class ApkVersion extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String apkUrl;

    private String apkVersion;

    private Byte apkStatus;
    
}