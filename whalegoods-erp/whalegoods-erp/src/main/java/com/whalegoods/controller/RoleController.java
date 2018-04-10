package com.whalegoods.controller;

import com.alibaba.fastjson.JSONArray;
import com.whalegoods.config.log.Log;
import com.whalegoods.config.log.Log.LOG_TYPE;
import com.whalegoods.entity.SysRole;
import com.whalegoods.entity.SysRoleMenu;
import com.whalegoods.entity.SysRoleUser;
import com.whalegoods.exception.MyException;
import com.whalegoods.service.MenuService;
import com.whalegoods.service.RoleMenuService;
import com.whalegoods.service.RoleService;
import com.whalegoods.service.RoleUserService;
import com.whalegoods.util.BeanUtil;
import com.whalegoods.util.JsonUtil;

/*import io.swagger.annotations.ApiOperation;*/
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 角色相关控制器
 * @author chencong
 *
 */
@Controller
@RequestMapping(value = "/role")
public class RoleController extends BaseController<Object> {

  @Autowired
  private RoleService roleService;

  @Autowired
  private RoleUserService roleUserService;

  @Autowired
  private MenuService menuService;

  @Autowired
  private RoleMenuService roleMenuService;

  @GetMapping(value = "showRole")
  @RequiresPermissions(value = "role:show")
  public String showRole(Model model){
    return "/system/role/roleList";
  }

  @GetMapping(value = "showRoleList")
  @ResponseBody
  @RequiresPermissions("role:show")
  public String showRoleList(SysRole role,Model model,String page,String limit){
   return roleService.show(role,Integer.valueOf(page),Integer.valueOf(limit));
  }

  @GetMapping(value = "showAddRole")
  public String addRole(Model model) {
    JSONArray jsonArray=menuService.getTreeUtil(null);
    model.addAttribute("menus",jsonArray.toJSONString());
    return "/system/role/add-role";
  }

 
  @Log(desc = "添加角色")
  @PostMapping(value = "addRole")
  @ResponseBody
  public JsonUtil addRole(SysRole sysRole,String[] menus){
    if(StringUtils.isEmpty(sysRole.getRoleName())){
      JsonUtil.error("角色名称不能为空");
    }
    JsonUtil j=new JsonUtil();
    try{
      roleService.insertSelective(sysRole);
      //操作role-menu data
      SysRoleMenu sysRoleMenu=new SysRoleMenu();
      sysRoleMenu.setRoleId(sysRole.getId());

      if(menus!=null)
      for(String menu:menus){
        sysRoleMenu.setMenuId(menu);
        roleMenuService.insert(sysRoleMenu);
      }
      j.setMsg("保存成功");
    }catch (MyException e){
      j.setMsg("保存失败");
      j.setFlag(false);
      e.printStackTrace();
    }
    return j;
  }

  @GetMapping(value = "updateRole")
  public String updateRole(String id,Model model,boolean detail){
    if (StringUtils.isNotEmpty(id)) {
      SysRole role = roleService.selectByPrimaryKey(id);
      model.addAttribute("role", role);
      JSONArray jsonArray=menuService.getTreeUtil(id);
      model.addAttribute("menus",jsonArray.toJSONString());
    }
    model.addAttribute("detail", detail);
    return "system/role/update-role";
  }


  @Log(desc = "更新角色")
  @PostMapping(value = "updateRole")
  @ResponseBody
  public JsonUtil updateUser(SysRole role,String[] menus) {
    JsonUtil jsonUtil = new JsonUtil();
    jsonUtil.setFlag(false);
    if (role == null) {
      jsonUtil.setMsg("获取数据失败");
      return jsonUtil;
    }
    try {
      SysRole oldRole = roleService.selectByPrimaryKey(role.getId());
      BeanUtil.copyNotNullBean(role, oldRole);
      roleService.updateByPrimaryKeySelective(oldRole);

      SysRoleMenu sysRoleMenu=new SysRoleMenu();
      sysRoleMenu.setRoleId(role.getId());
      List<SysRoleMenu> menuList=roleMenuService.selectByCondition(sysRoleMenu);
      for(SysRoleMenu sysRoleMenu1:menuList){
        roleMenuService.deleteByPrimaryKey(sysRoleMenu1);
      }
      if(menus!=null)
      for(String menu:menus){
        sysRoleMenu.setMenuId(menu);
        roleMenuService.insert(sysRoleMenu);
      }
      jsonUtil.setFlag(true);
      jsonUtil.setMsg("修改成功");
    } catch (MyException e) {
      jsonUtil.setMsg("修改失败");
      e.printStackTrace();
    }
    return jsonUtil;
  }


  @Log(desc = "删除角色",type = LOG_TYPE.DEL)
  @PostMapping(value = "del")
  @ResponseBody
  @RequiresPermissions("role:del")
  public JsonUtil del(String id) {
    if (StringUtils.isEmpty(id)) {
      return JsonUtil.error("获取数据失败");
    }
    SysRoleUser sysRoleUser=new SysRoleUser();
    sysRoleUser.setRoleId(id);
    JsonUtil j=new JsonUtil();
    try {
     int count= roleUserService.selectCountByCondition(sysRoleUser);
     if(count>0){
       return JsonUtil.error("已分配给用户，删除失败");
     }
        roleService.deleteByPrimaryKey(id);
     j.setMsg("删除成功");
    } catch (MyException e) {
      j.setMsg("删除失败");
      j.setFlag(false);
      e.printStackTrace();
    }
    return j;
  }

}