<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title></title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi" />
  <link rel="stylesheet" href="${re.contextPath}/plugin/layui/css/layui.css">
  <script type="text/javascript" src="${re.contextPath}/plugin/jquery/jquery-3.2.1.min.js"></script>
  <script type="text/javascript" src="${re.contextPath}/plugin/layui/layui.all.js" charset="utf-8"></script>
</head>

<body>
<div class="x-body">
  <form class="layui-form layui-form-pane" style="margin:20px;">
  <input value="${job.id}" type="hidden" name="id">
    <div class="layui-form-item">
      <label for="jobName" class="layui-form-label"><span class="x-red">*</span>任务名称</label>
      <div class="layui-input-inline"><input type="text"  id="jobName" name="jobName" value="${job.jobName}" lay-verify="required" autocomplete="off" class="layui-input"></div>
    </div>
    <div class="layui-form-item">
      <label for="jobCron" class="layui-form-label"><span class="x-red">*</span>Cron表达式</label>
      <div class="layui-input-inline"><input type="text"  id="jobCron" name="jobCron" value="${job.jobCron}"  lay-verify="required" autocomplete="off" class="layui-input"></div>
    </div>
    <div class="layui-form-item">
      <label for="execPath" class="layui-form-label"><span class="x-red">*</span>执行路径</label>
      <div class="layui-input-inline"><input type="text"  id="execPath" name="execPath"  value="${job.execPath}" lay-verify="required" autocomplete="off" class="layui-input"></div>
    </div>
    <div class="layui-form-item">
      <label for="jobDesc" class="layui-form-label"><span class="x-red">*</span>任务描述</label>
      <div class="layui-input-inline"><input type="text"  id="jobDesc" name="jobDesc" value="${job.jobDesc}"  lay-verify="required" autocomplete="off" class="layui-input"></div>
      <div class="layui-input-inline" style="padding-top: 10px;"><a href="http://cron.qqe2.com" target="_blank" style="color:blue;">Cron表达式生成器</a></div>
    </div>
          <div class="layui-form-item">
        <label class="layui-form-label">通知角色</label>
        <div class="layui-input-block">
          <#list boxJson as json>
         <input type="checkbox" name="role" lay-filter="check" value="${json.id}" title="${json.name}" <#if json.check?string=='true'>checked</#if>>
          </#list>
        </div>
      </div>
  <div style="width: 100%;height: 55px;background-color: white;border-top:1px solid #e6e6e6; position: fixed;bottom: 1px;margin-left:-20px;">
    <div class="layui-form-item" style=" float: right;margin-right: 30px;margin-top: 8px">
      <button  class="layui-btn layui-btn-normal" lay-filter="confirm" lay-submit="">确定</button>
      <button  class="layui-btn layui-btn-primary" id="close">取消</button>
    </div>
  </div>
  </form>
</div>
<script>  
  layui.use(['form','layer'], function(){
    $ = layui.jquery;
    var form = layui.form,layer = layui.layer;

   $('#close').click(function(){
     var index = parent.layer.getFrameIndex(window.name);
     parent.layer.close(index);
   });
    //监听提交
    form.on('submit(confirm)', function(data){
        var r=document.getElementsByName("role");
        var role=[];
        for(var i=0;i<r.length;i++){
          if(r[i].checked){
            role.push(r[i].value);
          }
        }
        data.field.role=role;
      $.ajax({
        url:'updateJob',
        type:'post',
        data:data.field,
        async:false,
        contentType : 'application/json',  
        data:JSON.stringify(data.field),
        traditional: true,
        success:function(d){
          if(d.result_code==0){
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
            window.parent.layui.table.reload('jobList');
            window.top.layer.msg(d.result_msg,{icon:6,time:1000});
          }else{
            layer.msg(d.result_msg,{icon:5});
          }},
          error:function(){
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
            window.top.layer.msg('请求失败',{icon:5,time:1000});
        }
      });
      return false;
    });
    form.render();
  });
</script>
</body>

</html>
