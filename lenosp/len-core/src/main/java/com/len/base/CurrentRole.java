package com.len.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 当前角色实体类
 * @author chencong
 *
 */
@Getter
@Setter
@ToString
public class CurrentRole {

    private String id;

    private String roleName;

    private String remark;

    public CurrentRole(String id, String roleName, String remark) {
        this.id = id;
        this.roleName = roleName;
        this.remark = remark;
    }
}
