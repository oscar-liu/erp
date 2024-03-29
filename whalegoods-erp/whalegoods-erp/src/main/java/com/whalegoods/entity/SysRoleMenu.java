package com.whalegoods.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SysRoleMenu extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private String roleId;

    private String menuId;

}