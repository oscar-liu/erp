package com.whalegoods.config.log;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.UnavailableSecurityManagerException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.whalegoods.common.CurrentUser;
import com.whalegoods.entity.SysLog;
import com.whalegoods.mapper.SysLogMapper;
import com.whalegoods.util.IpUtil;
import com.whalegoods.util.ShiroUtil;
import com.whalegoods.util.StringUtil;

/**
 * 为增删改添加监控
 * @author chencong
 *
 */
@Aspect
@Component
public class LogAspect {

    @Autowired
    private SysLogMapper logMapper;

    @Pointcut("@annotation(com.whalegoods.config.log.Log)")
    private void pointcut() {

    }

    @After("pointcut()")
    public void insertLogSuccess(JoinPoint jp){
        addLog(jp,getDesc(jp));
    }

    private void addLog(JoinPoint jp,String text){
        Log.LOG_TYPE type=getType(jp);
        SysLog log=new SysLog();
        RequestAttributes requestAttributes=RequestContextHolder.getRequestAttributes();
        //一些系统监控
        if(requestAttributes!=null){
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String ip= IpUtil.getIp(request);
            log.setIp(ip);
        }
        log.setId(StringUtil.getUUID());
        log.setCreateTime(new Date());
        log.setType(type.toString());
        log.setText(text);

        Object[] obj= jp.getArgs();
        StringBuffer buffer=new StringBuffer();
        if(obj!=null){
            for(int i=0;i<obj.length;i++){
                buffer.append("[参数"+(i+1)+":");
                buffer.append(JSON.toJSONString(obj[i]));
                buffer.append("]");
            }
        }
        log.setParam(buffer.toString());
        try {
            CurrentUser currentUser = ShiroUtil.getCurrentUser();
            log.setUserName(currentUser.getUsername());
        }catch (UnavailableSecurityManagerException e){
        }
        logMapper.insert(log);
    }

    /**
     * 记录异常
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value="pointcut()",throwing="e")
    public void afterException(JoinPoint joinPoint,Exception e){
        System.out.print("-----------afterException:"+e.getMessage());
        addLog(joinPoint,getDesc(joinPoint)+e.getMessage());
    }


    private String getDesc(JoinPoint joinPoint){
        MethodSignature methodName = (MethodSignature)joinPoint.getSignature();
        Method method = methodName.getMethod();
        return method.getAnnotation(Log.class).desc();
    }

    private Log.LOG_TYPE getType(JoinPoint joinPoint){
        MethodSignature methodName = (MethodSignature)joinPoint.getSignature();
        Method method = methodName.getMethod();
        return method.getAnnotation(Log.class).type();
    }
}

