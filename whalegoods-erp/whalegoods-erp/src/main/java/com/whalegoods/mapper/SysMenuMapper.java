package com.whalegoods.mapper;

import com.whalegoods.entity.SysMenu;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu,String> {

        /**获取元节点*/
    List<SysMenu> getMenuNotSuper();

    /**
     * 获取子节点
     * @return
     */
    List<SysMenu> getMenuChildren(String id);

    List<SysMenu> getMenuChildrenAll(String id);

    /**
     * 根据用户ID获取所有菜单
     * @author henrysun
     * 2018年5月3日 下午5:56:20
     */
    List<SysMenu> getUserMenu(String userId);
    

}