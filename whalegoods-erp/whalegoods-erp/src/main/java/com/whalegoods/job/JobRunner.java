package com.whalegoods.job;

import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.SysJob;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.SysJobService;
import com.whalegoods.util.ReflectionUtil;

/**
 * 应用启动自动执行任务
 * @author henrysun
 * 2018年5月24日 上午10:33:25
 */
public class JobRunner implements ApplicationRunner {
	
	@Autowired
	private SysJobService sysJobService;
	
    //加入Qulifier注解，通过名称注入bean
  @Autowired @Qualifier("Scheduler")
  private Scheduler scheduler;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<SysJob> jobList=sysJobService.selectListByObjCdt(new SysJob());
        for (SysJob sysJob : jobList) {
        	if(sysJob.getJobStatus()==1){
        		// 启动调度器  
    			try {
    				scheduler.start();
    			} catch (SchedulerException e1) {
    				throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
    			} 
    			//构建job信息
    			JobDetail jobDetail;
    			try {
    				jobDetail = JobBuilder.newJob(ReflectionUtil.getClass(sysJob.getExecPath()).getClass()).withIdentity(sysJob.getExecPath(),null).build();
    			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e1) {
    				throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
    			}
    			//表达式调度构建器(即任务执行的时间)
    			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(sysJob.getJobCron());
    			//按新的cronExpression表达式构建一个新的trigger
    			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(sysJob.getExecPath(),null).withSchedule(scheduleBuilder).build();
    			try {
    				scheduler.scheduleJob(jobDetail, trigger);
    			} catch (SchedulerException e) {
    				throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
    			}
        	}
		}
    }
}
