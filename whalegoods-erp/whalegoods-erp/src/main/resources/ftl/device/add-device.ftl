<!DOCTYPE html>
<html>

<head>
  <meta charset="UTF-8">
  <title>添加设备</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi" />
  <link rel="stylesheet" href="${re.contextPath}/plugin/layui/css/layui.css">
  <script type="text/javascript" src="${re.contextPath}/plugin/jquery/jquery-3.2.1.min.js"></script>
  <script type="text/javascript" src="${re.contextPath}/plugin/layui/layui.all.js" charset="utf-8"></script>
</head>

<body>
<div class="x-body">
  <form class="layui-form layui-form-pane" style="margin-left: 20px;">
    <div style="width:100%;height:400px;overflow: auto;">
    <div class="layui-form-item">
      <label for="deviceIdSupp" class="layui-form-label"><span class="x-red">*</span>设备编号（供应商）</label>
      <div class="layui-input-inline"><input type="text"  id="deviceIdSupp" name="deviceIdSupp"   lay-verify="required" autocomplete="off" class="layui-input"></div>
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
      <div style="height: 60px"></div>
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
  var flag,msg;
  layui.use(['form','layer'], function(){
    $ = layui.jquery;
    var form = layui.form,layer = layui.layer;
   $('#close').click(function(){
     var index = parent.layer.getFrameIndex(window.name);
     parent.layer.close(index);
   });
    //监听提交
    form.on('submit(add)', function(data){
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
            window.top.layer.msg(d.result_msg,{icon:6});
          }else{
            layer.msg(d.result_msg,{icon:5});
          }},
          error:function(){
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
            window.top.layer.msg('请求失败',{icon:5});
        }
      });
      return false;
    });
    form.render();
  });
</script>
</body>

</html>
