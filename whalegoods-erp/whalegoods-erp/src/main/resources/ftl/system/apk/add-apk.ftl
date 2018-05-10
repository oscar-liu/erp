<!DOCTYPE html>
<html>

<head>
  <meta charset="UTF-8">
  <title>上传APK</title>
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
    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
      <legend style="font-size:16px;">APK上传</legend>
    </fieldset>
      <div class="layui-input-inline">
    <div class="layui-upload-drag" style="margin-left:10%;" id="upApk">
      <i style="font-size:30px;" class="layui-icon"></i>
     <p style="font-size: 10px">点击上传，或将文件拖拽到此处</p>     
    </div>
      </div>
      <div class="layui-input-inline">
          <div  id="upApkNow" style="margin-top: 20px;margin-left: 50px">           
          </div>
      </div>
    </div>
    <div class="layui-form-item">
    <input type="hidden" id="hidApkUrl" name="apkUrl" value="" />
      <label for="uname" class="layui-form-label"><span class="x-red">*</span>版本号</label>
      <div class="layui-input-inline"><input type="text"  id="apkVersion" name="apkVersion"  lay-verify="required" autocomplete="off" class="layui-input"></div>
      <div id="ms" class="layui-form-mid layui-word-aux"><span class="x-red">*</span><span id="ums">将会成为唯一的版本号</span></div>
    </div>
    <div class="layui-form-item">
      <div style="height: 60px"></div>
    </div>
  <div style="width: 100%;height: 55px;background-color: white;border-top:1px solid #e6e6e6; position: fixed;bottom: 1px;margin-left:-20px;">
    <div class="layui-form-item" style=" float: right;margin-right: 30px;margin-top: 8px">
      <button  class="layui-btn layui-btn-normal" lay-filter="add" lay-submit=""> 增加</button>
      <button  class="layui-btn layui-btn-primary" id="close">取消</button>
    </div>
  </div>
  </form>
</div>
<script>
  var flag,msg;
  $(function(){
      $('#apkVersion').on("blur",function(){
        var apkVersion=$('#apkVersion').val();
        if(apkVersion!='') {
          $.ajax({
            url: 'checkApkVersion?apkVersion=' + apkVersion, 
            async: false, 
            type: 'get', 
            success: function (data) {
              flag = data.result_code;
              $('#ms').find('span').remove();
              if (!data.result_code==0) {
                $('#ms').append("<span style='color: red;'>"+data.result_msg+"</span>");
              }else{
                flag=true;
                $('#ms').append("<span style='color: green;'>版本号可用</span>");
              }},
              beforeSend:function(){
              $('#ms').find('span').remove();
              $('#ms').append("<span>验证ing</span>");
            }
          });
        }
      });
  });
  
  layui.use(['form','layer','upload'], function(){
    $ = layui.jquery;
    var form = layui.form,layer = layui.layer,upload = layui.upload;
    upload.render({
      elem: '#upApk',
      url: 'uploadApk',
      before: function(obj){
        //预读，不支持ie8
        obj.preview(function(index, file, result){
          $('#upApkNow').append('<p  width="100px" height="100px">'+file.name+'</p>');
        });
      },
      done: function(res){
       if(!res.result_code==0){
         layer.msg(res.result_msg,{icon: 5});
       }
       else{
    	   $('#hidApkUrl').val(res.data.file_url);
       }
      }
    });

   $('#close').click(function(){
     var index = parent.layer.getFrameIndex(window.name);
     parent.layer.close(index);
   });
    //监听提交
    form.on('submit(add)', function(data){
      $.ajax({
        url:'addApk',
        type:'post',
        data:data.field,
        async:false,
        traditional: true,
        success:function(d){
          if(d.result_code==0){
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
            window.parent.layui.table.reload('apkList');
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
