package com.whalegoods.job;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.whalegoods.entity.ReportByDeviceTotal;
import com.whalegoods.service.ReportByDeviceService;
import com.whalegoods.service.ReportByDeviceTotalService;
import com.whalegoods.util.DateUtil;

/**
 * 每天0点05分统计昨天的按设备报表数据（统计report_by_device表）
 * @author henrysun
 * 2018年6月21日 下午2:34:15
 */
public class ReportByDeviceTotalJob implements BaseJob{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ReportByDeviceService reportByDeviceService;
	
	@Autowired
	private ReportByDeviceTotalService reportByDeviceTotalService;
	
    public ReportByDeviceTotalJob() {}
  
    @SuppressWarnings("static-access")
	public void execute(JobExecutionContext context) {
    	logger.info("开始执行任务：每天0点05分统计昨天的按设备报表数据（统计report_by_device表）");
    	Map<String,Object> mapCdt=new HashMap<>();
    	//获取昨天的日期，格式：yyyy-MM-dd
    	Calendar calendar = new GregorianCalendar();
    	String yesterday=DateUtil.getMoveDate(new Date(),calendar.DATE,-1);
    	mapCdt.put("orderDay",yesterday);
    	List<ReportByDeviceTotal> list=reportByDeviceService.selectListGroupByOrderDay(mapCdt);
    	logger.info("查询到{}条数据",list.size());
    	//批量插入到report_by_device表中
    	if(list.size()>0){
        	reportByDeviceTotalService.insertBatch(list);	
    	}
    }

}
