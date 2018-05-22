package com.whalegoods.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 任务表实体类
 * @author henrysun
 * 2018年5月22日 上午10:53:46
 */
@Getter
@Setter
@ToString
public class SysJob extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private String jobName;

    private String jobCron;
    
    private String execPath;

    private Byte jobStatus;
    
    private Byte switchStatus;

    private String jobDesc;
    
    private String role[];
}