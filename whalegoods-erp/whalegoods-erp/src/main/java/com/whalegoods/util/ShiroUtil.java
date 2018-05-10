package com.whalegoods.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.whalegoods.common.CurrentUser;

/**
 * shiro工具类
 * @author henrysun
 * 2018年5月5日 上午10:12:43
 */
public class ShiroUtil {

    public static Subject getSubject(){
        return SecurityUtils.getSubject();
    }

    public static Session getSession(){
        return getSubject().getSession();
    }
    
    public static CurrentUser getCurrentUser(){
        return (CurrentUser) getSession().getAttribute("currentUser");
    }
    
    /**
     * 获取当前登陆用户的用户ID
     * @author henrysun
     * 2018年5月7日 上午11:53:27
     */
    public static String getCurrentUserId(){
        return getCurrentUser().getId();
    }

}
