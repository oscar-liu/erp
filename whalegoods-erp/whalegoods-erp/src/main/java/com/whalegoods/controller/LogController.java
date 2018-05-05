package com.whalegoods.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.whalegoods.entity.SysLog;
import com.whalegoods.exception.BizApiException;
import com.whalegoods.mapper.SysLogMapper;
import com.whalegoods.util.JsonUtil;
import com.whalegoods.util.ReType;

/**
 * 系统日志API
 * @author henrysun
 * 2018年5月4日 下午12:46:10
 */
@Controller
@RequestMapping(value = "/log")
public class LogController {
	
    private static final Logger logger= Logger.getLogger(LogController.class);
    
    @Autowired
    private SysLogMapper logMapper;

    @GetMapping(value = "showLog")
    public String showMenu(Model model){
        return "/system/log/logList";
    }

    /**
     * 日志监控
     * @param sysLog
     * @param page
     * @param limit
     * @return
     */
    @GetMapping(value = "showLogList")
    @ResponseBody
    public String showLog(SysLog sysLog, String page, String limit){
        List<SysLog> tList=null;
        Page<SysLog> tPage= PageHelper.startPage(Integer.valueOf(page),Integer.valueOf(limit));
        try{
            tList=logMapper.selectListByObjCdt(sysLog);
        }catch (BizApiException e){
            logger.error("class:LogController ->method:showLog->message:"+e.getMessage());
        }
        ReType reType=new ReType(tPage.getTotal(),tList);
        return JSON.toJSONString(reType);
    }

    /**
     * 删除log
     * @param
     * @return
     */
    @PostMapping(value = "del")
    @ResponseBody
    public JsonUtil del(String[] ids){
        JsonUtil j=new JsonUtil();
        String msg="删除成功";
        try{
            for(String id:ids)
            logMapper.deleteById(id);
        }catch (BizApiException e){
            msg="删除失败";
            logger.error(msg+e.getMessage());
        }
        j.setMsg(msg);
        return j;
    }


}
