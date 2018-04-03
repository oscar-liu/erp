package com.whalegoods.core.filter;

import com.whalegoods.base.CurrentUser;
import com.whalegoods.service.MenuService;
import com.whalegoods.service.SysUserService;

import java.io.IOException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 拦截器 校验用户是否已授权 未授权返回到登录界面
 * @author henry-sun
 *
 */
public class PermissionFilter extends AuthorizationFilter {

  @Autowired
  private SysUserService userService;

  @Autowired
  private MenuService menuService;

  @Override
  protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse,
      Object o) throws Exception {
    String[] roles=(String[])o;

    Subject sub = getSubject(servletRequest, servletResponse);
    Session session= sub.getSession();
    CurrentUser user= (CurrentUser) session.getAttribute("curentUser");
    if(user==null) {
      return false;
    }
    return true;
  }

  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
      throws IOException {
      saveRequest(request);
      WebUtils.issueRedirect(request, response, "/goLogin");
    return false;
  }
}
