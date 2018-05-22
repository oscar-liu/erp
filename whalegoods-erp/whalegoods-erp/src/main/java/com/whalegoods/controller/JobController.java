package com.whalegoods.controller;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.Checkbox;
import com.whalegoods.entity.SysJob;
import com.whalegoods.entity.SysJobRole;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.exception.BizApiException;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.SysJobRoleService;
import com.whalegoods.service.SysJobService;
import com.whalegoods.util.ReType;
import com.whalegoods.util.RegexUtil;
import com.whalegoods.util.ShiroUtil;
import com.whalegoods.util.StringUtil;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
	   */
	  @PostMapping(value = "addJob")
	  @ResponseBody
	  public ResBody addJob(SysJob sysJob,String[] role) throws SystemException {
		if(!RegexUtil.isValidExpression(sysJob.getJobCron())){
			throw  new BizApiException(ConstApiResCode.ILLEGAL_CRON_EXPRESSION);
		}
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
		 if(!RegexUtil.isValidExpression(sysJob.getJobCron())){
			 throw  new BizApiException(ConstApiResCode.ILLEGAL_CRON_EXPRESSION);
		 }
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		  sysJobService.updateByObjCdt(sysJob);
		  SysJob oldJob = sysJobService.selectById(sysJob.getId());
		  SysJobRole sysJobRole =new SysJobRole();
		  sysJobRole.setJobId(oldJob.getId());
	      List<SysJobRole> keyList=sysJobRoleService.selectListByObjCdt(sysJobRole);
	      for(SysJobRole sysJobRole2 :keyList){
	        sysJobRoleService.deleteById(sysJobRole2.getId());
	      }
	      if(sysJob.getRole()!=null){
	        for(String r:sysJob.getRole()){
	        	sysJobRole.setId(StringUtil.getUUID());
	        	sysJobRole.setRoleId(r);
	          sysJobRoleService.insert(sysJobRole);
	        }
	      }
		  return resBody;
	  }

	  /**
	   * 删除任务接口
	   * @author henrysun
	   * 2018年4月26日 下午3:32:39
	   */
	  @PostMapping(value = "/delJob")
	  @ResponseBody
	  @RequiresPermissions("job:del")
	  public ResBody delJob(@RequestParam String id) {
	    ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
	    sysJobService.deleteById(id);
	   return resBody;
	  }

	  
	  /**
	   * 更新任务启动关闭状态
	   * @author henrysun
	   * 2018年5月6日 上午10:30:07
	   */
	  @PostMapping(value = "updateJobStatus")
	  @ResponseBody
	  public ResBody updateJobStatus(@RequestParam String id,@RequestParam(name="jobStatus") Boolean isCheck) {
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
