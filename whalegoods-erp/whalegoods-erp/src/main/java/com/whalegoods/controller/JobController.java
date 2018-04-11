package com.whalegoods.controller;

import com.whalegoods.config.log.Log;
import com.whalegoods.config.log.Log.LOG_TYPE;
import com.whalegoods.core.quartz.JobTask;
import com.whalegoods.entity.SysJob;
import com.whalegoods.exception.BizApiException;
import com.whalegoods.service.JobService;
import com.whalegoods.util.BeanUtil;
import com.whalegoods.util.JsonUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 任务调度API
 * @author chencong
 * 2018年4月11日 上午9:29:35
 */
@Controller
@RequestMapping("/job")
public class JobController extends BaseController<SysJob> {

  @Autowired
  JobService jobService;

  @Autowired
  JobTask jobTask;

  @GetMapping(value = "showJob")
  @RequiresPermissions("job:show")
  public String showUser(Model model) {
    return "/system/job/jobList";
  }

  @GetMapping(value = "showJobList")
  @ResponseBody
  @RequiresPermissions("job:show")
  public String showUser(Model model, SysJob job, String page, String limit) {
    return jobService.show(job,Integer.valueOf(page),Integer.valueOf(limit));
  }

  @GetMapping(value = "showAddJob")
  public String addJob(Model model) {
    return "/system/job/add-job";
  }

  @Log(desc = "添加任务")
  @PostMapping(value = "addJob")
  @ResponseBody
  public JsonUtil addJob(SysJob job) {
    JsonUtil j=new JsonUtil();
    String msg="保存成功";
    job.setStatus(false);
    try {
      jobService.insertSelective(job);
    }catch (BizApiException e){
      msg="保存失败";
      j.setFlag(false);
      e.printStackTrace();
    }
    j.setMsg(msg);
    return j;
  }

  @GetMapping(value = "updateJob")
  public String updateJob(String id, Model model, boolean detail) {
    if (StringUtils.isNotEmpty(id)) {
      SysJob job = jobService.selectByPrimaryKey(id);
      model.addAttribute("job", job);
    }
    model.addAttribute("detail", detail);
    return "system/job/update-job";
  }


  
  @Log(desc = "更新任务",type = LOG_TYPE.UPDATE)
  @PostMapping(value = "updateJob")
  @ResponseBody
  public JsonUtil updateJob(SysJob job){
    JsonUtil j=new JsonUtil();
    j.setFlag(false);
    if (job == null) {
      j.setMsg("获取数据失败");
      return j;
    }
    if(jobTask.checkJob(job)){
      j.setMsg("已经启动任务无法更新,请停止后更新");
      return j;
    }
    try{
      SysJob oldJob=jobService.selectByPrimaryKey(job.getId());
      BeanUtil.copyNotNullBean(job, oldJob);
      jobService.updateByPrimaryKey(oldJob);
      j.setFlag(true);
      j.setMsg("修改成功");
    }catch (BizApiException e){
      j.setMsg("更新失败");
      e.printStackTrace();
    }
    return j;
  }

  @Log(desc = "删除任务",type = LOG_TYPE.DEL)
  @PostMapping(value = "del")
  @ResponseBody
  @RequiresPermissions("job:del")
  public JsonUtil del(String id){
    JsonUtil j=new JsonUtil();
    j.setFlag(false);
    if (StringUtils.isEmpty(id)) {
      j.setMsg("获取数据失败");
      return j;
    }
    try{
      SysJob job=jobService.selectByPrimaryKey(id);
      boolean flag=jobTask.checkJob(job);
      if((flag&&!job.getStatus())||!flag&&job.getStatus()){
        j.setMsg("您任务表状态和web任务状态不一致,无法删除");
        return j;
      }
      if(flag){
        j.setMsg("该任务处于启动中，无法删除");
        return j;
      }
      jobService.deleteByPrimaryKey(id);
      j.setFlag(true);
      j.setMsg("任务删除成功");
    }catch (BizApiException e){
      j.setMsg("任务删除异常");
      e.printStackTrace();
    }
    return j;
  }


  @Log(desc = "启动任务")
  @PostMapping(value = "startJob")
  @ResponseBody
  @RequiresPermissions("job:start")
  public JsonUtil startJob(String id){
    JsonUtil j=new JsonUtil();
    String msg=null;
    if(StringUtils.isEmpty(id)){
      j.setMsg("获取数据失败");
      j.setFlag(false);
      return j;
    }
    try {
      SysJob job = jobService.selectByPrimaryKey(id);
      jobTask.startJob(job);
      job.setStatus(true);
      jobService.updateByPrimaryKey(job);
      msg="启动成功";
    }catch (BizApiException e){
      e.printStackTrace();
    }
    j.setMsg(msg);
    return j;
  }

  @Log(desc = "停止任务")
  @PostMapping(value = "endJob")
  @ResponseBody
  @RequiresPermissions("job:end")
  public JsonUtil endJob(String id){
    JsonUtil j=new JsonUtil();
    String msg=null;
    if(StringUtils.isEmpty(id)){
      j.setMsg("获取数据失败");
      j.setFlag(false);
      return j;
    }
    try {
      SysJob job = jobService.selectByPrimaryKey(id);
      jobTask.remove(job);
      job.setStatus(false);
      jobService.updateByPrimaryKey(job);
      msg="停止成功";
    }catch (BizApiException e){
      e.printStackTrace();
    }
    j.setMsg(msg);
    return j;
  }

}
