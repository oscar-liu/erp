package com.whalegoods.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统角色用户关系表实体类
 * @author henrysun
 * 2018年5月4日 下午1:06:27
 */
@Getter
@Setter
@ToString
public class SysRoleUser extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String userId;

    private String roleId;

}