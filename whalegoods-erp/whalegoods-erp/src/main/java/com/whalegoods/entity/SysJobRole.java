package com.whalegoods.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 任务角色关系表实体类
 * @author henrysun
 * 2018年5月22日 上午10:53:46
 */
@Getter
@Setter
@ToString
public class SysJobRole extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private String roleId;

    private String jobId;
    
}