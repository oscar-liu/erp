package com.whalegoods.service;

import com.alibaba.fastjson.JSONArray;
/*import com.whalegoods.base.BaseService;*/
import com.whalegoods.entity.SysMenu;

import java.util.List;

/**
 * 系统菜单相关业务逻辑接口层
 * @author henrysun
 * 2018年5月2日 下午3:51:18
 */
public interface MenuService extends BaseService<SysMenu,String> {
	
	  List<SysMenu> getMenuNotSuper();

	  List<SysMenu> getMenuChildren(String id);

	  public JSONArray getMenuJson(String roleId);

	  public JSONArray getMenuJsonList();

	  List<SysMenu> getMenuChildrenAll(String id);

	  public JSONArray getTreeUtil(String roleId);

	  List<SysMenu> getUserMenu(String id);

	  public JSONArray getMenuJsonByUser(List<SysMenu> menuList);
}
