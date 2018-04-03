package com.whalegoods.service;

import com.alibaba.fastjson.JSONArray;
/*import com.whalegoods.base.BaseService;*/
import com.whalegoods.entity.SysMenu;

import java.util.List;

/**
 * @author zhuxiaomeng
 * @date 2017/12/12.
 * @email 154040976@qq.com
 */
public interface MenuService extends BaseService<SysMenu,String> {

  List<SysMenu> getMenuNotSuper();

  @Override
  int insert(SysMenu menu);

  @Override
  SysMenu selectByPrimaryKey(String id);

  List<SysMenu> getMenuChildren(String id);

  public JSONArray getMenuJson(String roleId);

  public JSONArray getMenuJsonList();

  List<SysMenu> getMenuChildrenAll(String id);

  public JSONArray getTreeUtil(String roleId);

  List<SysMenu> getUserMenu(String id);

  public JSONArray getMenuJsonByUser(List<SysMenu> menuList);
}
