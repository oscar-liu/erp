package com.whalegoods.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.whalegoods.config.job.QuartzConfig;
import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.Checkbox;
import com.whalegoods.entity.SysJob;
import com.whalegoods.entity.SysJobRole;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.SysJobRoleService;
import com.whalegoods.service.SysJobService;
import com.whalegoods.util.ReType;
import com.whalegoods.util.ReflectionUtil;
import com.whalegoods.util.ShiroUtil;
import com.whalegoods.util.StringUtil;

/**
 * 后台系统任务管理接口
 * @author henrysun
 * 2018年4月25日 下午7:59:49
 */
@Controller
@RequestMapping(value = "/job")
public class JobController {
	
	  @Autowired
	  SysJobService sysJobService;
	  
	  @Autowired
	  SysJobRoleService sysJobRoleService;
	  
	  @Autowired
	  private QuartzConfig config;

	  /**
	   * 跳转到任务列表页面
	   * @author henrysun
	   * 2018年4月26日 下午3:29:12
	   */
	  @GetMapping(value = "showJob")
	  @RequiresPermissions("job:show")
	  public String showJob(Model model) {
	    return "/system/job/jobList";
	  }

	  /**
	   * 查询任务列表接口
	   * @author henrysun
	   * 2018年4月26日 下午3:29:23
	   */
	  @GetMapping(value = "showJobList")
	  @ResponseBody
	  @RequiresPermissions("job:show")
	  public ReType showJobList(Model model, SysJob sysJob, String page, String limit) {
	    return sysJobService.selectByPage(sysJob,Integer.valueOf(page),Integer.valueOf(limit));
	  }

	  /**
	   * 跳转到添加任务页面
	   * @author henrysun
	   * 2018年4月26日 下午3:30:10
	   */
	  @GetMapping(value = "showAddJob")
	  public String showAddJob(Model model) {
		List<Checkbox> checkboxList=sysJobService.getJobRoleByJson(null);
		model.addAttribute("boxJson",checkboxList);
	    return "/system/job/add-job";
	  }

	  /**
	   * 添加任务接口
	   * @author henrysun
	   * 2018年4月26日 下午3:30:25
	 * @throws SystemException 
	 * @throws Exception 
	   */
	  @PostMapping(value = "addJob")
	  @ResponseBody
	  public ResBody addJob(SysJob sysJob,String[] role) throws SystemException {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		sysJob.setId(StringUtil.getUUID());
    	String currentUserId=ShiroUtil.getCurrentUserId();
    	sysJob.setCreateBy(currentUserId);
    	sysJob.setUpdateBy(currentUserId);
		sysJobService.insert(sysJob);
		SysJobRole sysJobRole=new SysJobRole();
		sysJobRole.setJobId(sysJob.getId());
		 for(String r:role){
			 sysJobRole.setId(StringUtil.getUUID());
			 sysJobRole.setRoleId(r);
			 sysJobRoleService.insert(sysJobRole);
		}
	        // 启动调度器  
	        try {
	        	config.scheduler().start();
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
	        	config.scheduler().scheduleJob(jobDetail, trigger);
	        	config.scheduler().pauseJob(JobKey.jobKey(sysJob.getExecPath(),null));
			} catch (SchedulerException e) {
				throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
			}	        
	    return resBody;
	  }

	  /**
	   * 跳转到更新任务页面
	   * @author henrysun
	   * 2018年4月26日 下午3:30:35
	   */
	  @GetMapping(value = "showUpdateJob")
	  public String showUpdateJob(@RequestParam String id, Model model){
	      //用户-角色
	     List<Checkbox> checkboxList=sysJobService.getJobRoleByJson(id);
	      SysJob sysJob = sysJobService.selectById(id);
	      model.addAttribute("job", sysJob);
	      model.addAttribute("boxJson", checkboxList);
	    return "/system/job/update-job";
	  }


	  /**
	   * 更新任务接口
	   * @author henrysun
	   * 2018年4月26日 下午3:30:49
	 * @throws SystemException 
	   */
	  @PostMapping(value = "updateJob")
	  @ResponseBody
	  public ResBody updateJob(@RequestBody SysJob sysJob) throws SystemException {
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		  String sysJobId=sysJob.getId();
		  //先更新sys_job表
		  sysJobService.updateByObjCdt(sysJob);
		  //删除sys_job_role表相关数据
		  sysJobRoleService.deleteByJobId(sysJobId);
		  //生成新的关联信息
	      if(sysJob.getRole()!=null){
	    	 List<SysJobRole> jobRoles=new ArrayList<>();
	    	 SysJobRole sysJobRole=null;
	        for(String r:sysJob.getRole()){
	        	sysJobRole=new SysJobRole();
	        	sysJobRole.setId(StringUtil.getUUID());
	        	sysJobRole.setRoleId(r);
	        	sysJobRole.setJobId(sysJobId);
	        	jobRoles.add(sysJobRole);
	        }
	        sysJobRoleService.insertBatch(jobRoles);
	      }
          TriggerKey triggerKey = TriggerKey.triggerKey(sysJob.getExecPath(),null);
          // 表达式调度构建器
          CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(sysJob.getJobCron());
          try {
              CronTrigger trigger = (CronTrigger) config.scheduler().getTrigger(triggerKey);
              // 按新的cronExpression表达式重新构建trigger
              trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
              // 按新的trigger重新设置job执行
              config.scheduler().rescheduleJob(triggerKey, trigger);
              //如果状态是关闭，则暂停任务
              SysJob sysJob2=sysJobService.selectById(sysJobId);
              if(sysJob2.getSwitchStatus()==2){
            	  config.scheduler().pauseJob(JobKey.jobKey(sysJob.getExecPath(),null));
              }
		} catch (SchedulerException e) {
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		}
		return resBody;
	  }

	  /**
	   * 删除任务接口
	   * @author henrysun
	   * 2018年4月26日 下午3:32:39
	 * @throws SystemException 
	   */
	  @PostMapping(value = "/delJob")
	  @ResponseBody
	  @RequiresPermissions("job:del")
	  public ResBody delJob(@RequestParam String id) throws SystemException {
	    ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
	    SysJob sysJob = sysJobService.selectById(id);
	    try {
		    config.scheduler().pauseTrigger(TriggerKey.triggerKey(sysJob.getExecPath(), null));
		    config.scheduler().unscheduleJob(TriggerKey.triggerKey(sysJob.getExecPath(), null));
		    config.scheduler().deleteJob(JobKey.jobKey(sysJob.getExecPath(), null));
		} catch (SchedulerException e) {
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		}
	    sysJobService.deleteById(id);
	   return resBody;
	  }

	  
	  /**
	   * 更新任务启动关闭状态
	   * @author henrysun
	   * 2018年5月6日 上午10:30:07
	 * @throws SystemException 
	   */
	  @PostMapping(value = "updateJobStatus")
	  @ResponseBody
	  public ResBody updateJobStatus(@RequestParam String id,@RequestParam(name="jobStatus") Boolean isCheck) throws SystemException {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		SysJob sysJob=new SysJob();
		sysJob.setId(id);
		//1开启 2关闭
		if(isCheck){
			sysJob.setJobStatus((byte) 1);
		}
		else{
			sysJob.setJobStatus((byte) 2);
		}
		sysJobService.updateByObjCdt(sysJob);
		sysJob=sysJobService.selectById(id);
		try {
			config.scheduler().start();
			if(sysJob.getJobStatus()==1){
				config.scheduler().resumeJob(JobKey.jobKey(sysJob.getExecPath(),null));
			}
			if(sysJob.getJobStatus()==2){
				config.scheduler().pauseJob(JobKey.jobKey(sysJob.getExecPath(),null));
			}
		} catch (SchedulerException e) {
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		}
	    return resBody;
	  }
	  
	  /**
	   * 更新任务通知开启关闭状态
	   * @author henrysun
	   * 2018年5月6日 上午10:30:07
	   */
	  @PostMapping(value = "updateSwitchStatus")
	  @ResponseBody
	  public ResBody updateSwitchStatus(@RequestParam String id,@RequestParam(name="switchStatus") Boolean isCheck) {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		SysJob sysJob=new SysJob();
		sysJob.setId(id);
		//1开启 2关闭
		if(isCheck){
			sysJob.setSwitchStatus((byte) 1);
		}
		else{
			sysJob.setSwitchStatus((byte) 2);
		}
		sysJobService.updateByObjCdt(sysJob);
	    return resBody;
	  }
	  


}
