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

import com.whalegoods.entity.ReportByDevice;
import com.whalegoods.service.ReportBaseService;
import com.whalegoods.service.ReportByDeviceService;
import com.whalegoods.util.DateUtil;

/**
 * 每天0点03分统计昨天的基本报表数据（按设备）
 * @author henrysun
 * 2018年6月21日 下午2:34:15
 */
public class ReportByDeviceJob implements BaseJob{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ReportBaseService reportBaseService;
	
	@Autowired
	private ReportByDeviceService reportByDeviceService;
	
    public ReportByDeviceJob() {}
  
    @SuppressWarnings("static-access")
	public void execute(JobExecutionContext context) {
    	logger.info("开始执行任务：每天0点03分统计昨天的基本报表数据（按设备）");
    	Map<String,Object> mapCdt=new HashMap<>();
    	//获取昨天的日期，格式：yyyy-MM-dd
    	Calendar calendar = new GregorianCalendar();
    	String yesterday=DateUtil.getMoveDate(new Date(),calendar.DATE,-1);
    	mapCdt.put("orderDay",yesterday);
    	List<ReportByDevice> list=reportBaseService.selectListGroupByDevice(mapCdt);
    	logger.info("查询到{}条数据",list.size());
    	//批量插入到report_by_device表中
    	if(list.size()>0){
    		reportByDeviceService.insertBatch(list);
    	}
    }

}
