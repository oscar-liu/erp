package com.whalegoods.config.shiro;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.whalegoods.core.filter.PermissionFilter;
import com.whalegoods.core.filter.VerfityCodeFilter;

/**
 * shiro配置
 * @author henry-sun
 *
 */
@Configuration
public class ShiroConfig {

  @Bean
  public RetryLimitCredentialsMatcher getRetryLimitCredentialsMatcher(){
    RetryLimitCredentialsMatcher rm=new RetryLimitCredentialsMatcher(getCacheManager(),"5");
    rm.setHashAlgorithmName("md5");
    rm.setHashIterations(4);
    return rm;

  }
  @Bean(name = "loginRealm")
  public LoginRealm getLoginRealm(){
    LoginRealm realm= new LoginRealm();
    realm.setCredentialsMatcher(getRetryLimitCredentialsMatcher());
    return realm;
  }

  @Bean
  public EhCacheManager getCacheManager(){
    EhCacheManager ehCacheManager=new EhCacheManager();
    ehCacheManager.setCacheManagerConfigFile("classpath:ehcache/ehcache.xml");
    return ehCacheManager;
  }

  @Bean
  public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
    return new LifecycleBeanPostProcessor();
  }

  @Bean(name="securityManager")
  public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("loginRealm") LoginRealm loginRealm){
    DefaultWebSecurityManager dwm=new DefaultWebSecurityManager();
    dwm.setRealm(loginRealm);
    dwm.setCacheManager(getCacheManager());
    return dwm;
  }

  @Bean
  public PermissionFilter getPermissionFilter(){
    PermissionFilter pf=new PermissionFilter();
    return pf;
  }

  @Bean
  public VerfityCodeFilter getVerfityCodeFilter(){
    VerfityCodeFilter vf= new VerfityCodeFilter();
    vf.setFailureKeyAttribute("shiroLoginFailure");
    vf.setJcaptchaParam("code");
    vf.setVerfitiCode(true);
    return vf;
  }

  @Bean(name = "shiroFilter")
  public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager){
    ShiroFilterFactoryBean sfb = new ShiroFilterFactoryBean();
    sfb.setSecurityManager(securityManager);
    sfb.setLoginUrl("/login");
    sfb.setUnauthorizedUrl("/goLogin");
    Map<String, Filter> filters=new HashMap<>();
    filters.put("per",getPermissionFilter());
    filters.put("verCode",getVerfityCodeFilter());
    sfb.setFilters(filters);
    Map<String, String> filterMap = new LinkedHashMap<>();
    filterMap.put("/login","verCode,anon");
    filterMap.put("/getCode","anon");
    filterMap.put("/logout","logout");
    filterMap.put("/plugin/**","anon");
    filterMap.put("/user/**","per");
    //移动客户端请求的接口不做shiro校验
    filterMap.put("/v1/**","anon");
    filterMap.put("/v2/**","anon");
    filterMap.put("/v3/**","anon");
    filterMap.put("/v4/**","anon");
    filterMap.put("/v5/**","anon");
    sfb.setFilterChainDefinitionMap(filterMap);
    return sfb;
  }

  @Bean
  public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
    DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
    advisorAutoProxyCreator.setProxyTargetClass(true);
    return advisorAutoProxyCreator;
  }

  @Bean
  public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(@Qualifier("securityManager") DefaultWebSecurityManager securityManager){
    AuthorizationAttributeSourceAdvisor as=new AuthorizationAttributeSourceAdvisor();
    as.setSecurityManager(securityManager);
    return as;
  }

  @Bean
  public FilterRegistrationBean delegatingFilterProxy(){
    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
    DelegatingFilterProxy proxy = new DelegatingFilterProxy();
    proxy.setTargetFilterLifecycle(true);
    proxy.setTargetBeanName("shiroFilter");

    filterRegistrationBean.setFilter(proxy);
    return filterRegistrationBean;
  }


}
