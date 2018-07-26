<!DOCTYPE html>
<html>

<head>
  <meta charset="UTF-8">
  <title>添加设备</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi" />
  <link rel="stylesheet" href="${re.contextPath}/plugin/layui/css/layui.css">
  <link rel="stylesheet" href="${re.contextPath}/plugin/select2/css/select2.css">
  <script type="text/javascript" src="${re.contextPath}/plugin/jquery/jquery-3.2.1.min.js"></script>
  <script type="text/javascript" src="${re.contextPath}/plugin/layui/layui.all.js" charset="utf-8"></script>
  <script type="text/javascript" src="${re.contextPath}/plugin/select2/js/select2.min.js"></script>
  <script type="text/javascript" src="${re.contextPath}/plugin/select2/js/zh-CN.js"></script>
      <style type="text/css">
    .select2-container .select2-selection--single{  
      height:37px;  
      line-height: 37px;
    }
</style>
</head>

<body>
<div class="x-body">
  <form class="layui-form layui-form-pane" style="margin: 20px;">
    <div class="layui-form-item">
          <div class="layui-inline">
     <label for="sltDeviceModel" class="layui-form-label"><span class="x-red">*</span>所属型号</label>
      <div class="layui-input-inline">
       <select id="sltDeviceModel" name="sltDeviceModel" lay-verify="required"  lay-ignore>
     <option value="">直接选择或搜索选择</option>
  	<#list modelList as model>
          <option value="${model.id}">${model.modelName}</option>
    </#list>
    </select>
      </div>
    </div>
  </div>
    <div class="layui-form-item">
    <input  type="hidden" name="id">
      <label for="deviceIdSupp" class="layui-form-label"><span class="x-red">*</span>设备编号</label>
      <div class="layui-input-inline"><input type="text"  id="deviceIdSupp" name="deviceIdSupp" lay-verify="required" autocomplete="off" class="layui-input"></div>
    </div>
    <div class="layui-form-item">
      <label for="shortName" class="layui-form-label"><span class="x-red">*</span>点位短名</label>
      <div class="layui-input-inline"><input type="text"  id="shortName" name="shortName"  lay-verify="required" autocomplete="off" class="layui-input"></div>
    </div>
    <div class="layui-form-item">
      <label for="location" class="layui-form-label"><span class="x-red">*</span>点位地址</label>
      <div class="layui-input-inline"><input type="text"  id="location" name="location"  lay-verify="required" autocomplete="off" class="layui-input"></div>
    </div>
    <div class="layui-form-item">
      <label for="signCode" class="layui-form-label"><span class="x-red">*</span>签到码</label>
      <div class="layui-input-inline"><input type="text"  id="signCode" name="signCode"  lay-verify="required" autocomplete="off" class="layui-input"></div>
    </div>
    <div class="layui-form-item">
      <label for="devicePwd" class="layui-form-label"><span class="x-red">*</span>管理密码</label>
      <div class="layui-input-inline"><input type="password"  id="devicePwd" name="devicePwd"  lay-verify="required|number|eightlength" autocomplete="off" class="layui-input"></div>
    </div>
  <div style="width: 100%;height: 55px;background-color: white;border-top:1px solid #e6e6e6; position: fixed;bottom: 1px;margin-left:-20px;">
    <div class="layui-form-item" style=" float: right;margin-right: 30px;margin-top: 8px">
      <button  class="layui-btn layui-btn-normal" lay-filter="add" lay-submit="">增加</button>
      <button  class="layui-btn layui-btn-primary" id="close">取消</button>
    </div>
  </div>
  </form>
</div>
<script>

$(function(){
	 $('#sltDeviceModel').select2();
 });

  layui.use(['form','layer'], function(){
    $ = layui.jquery;
    var form = layui.form,layer = layui.layer;
   $('#close').click(function(){
     var index = parent.layer.getFrameIndex(window.name);
     parent.layer.close(index);
   });
   
   //自定义验证规则
   form.verify({
	  eightlength: function(value){
       if(value.length!=8){
         return "密码必须是八位数";
       }
   }});
   
    //监听提交
    form.on('submit(add)', function(data){
     data.field.deviceModelId=$('#sltDeviceModel').val();
      $.ajax({
        url:'addDevice',
        type:'post',
        data:data.field,
        async:false,
        traditional: true,
        success:function(d){
          if(d.result_code==0){
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
            window.parent.layui.table.reload('deviceList');
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
