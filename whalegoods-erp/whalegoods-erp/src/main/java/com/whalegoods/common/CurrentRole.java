package com.whalegoods.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 当前角色实体类
 * @author henrysun
 * 2018年5月2日 上午11:21:38
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
