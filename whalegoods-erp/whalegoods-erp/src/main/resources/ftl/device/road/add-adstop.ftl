<!DOCTYPE html>
<html>

<head>
  <meta charset="UTF-8">
  <title>添加顶部广告</title>
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
  <form class="layui-form layui-form-pane" style="margin-left: 20px;">
    <div style="width:100%;height:400px;overflow: auto;">
    <div class="layui-form-item">
    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
      <legend style="font-size:16px;">大图上传</legend>
    </fieldset>
      <div class="layui-input-inline">
    <div class="layui-upload-drag" style="margin-left:10%;" id="upBigPic">
      <i style="font-size:30px;" class="layui-icon"></i>
     <p style="font-size: 10px">点击上传，或将文件拖拽到此处</p>     
    </div>
      </div>
      <div class="layui-input-inline">
          <div  id="upBigPicNow" style="margin-top: 20px;margin-left: 50px">
            <img src="${re.contextPath}/plugin/x-admin/images/bg.png" width="100px" height="100px" class="layui-upload-img">
          </div>

      </div>
    </div>
    <div class="layui-form-item">
    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
      <legend style="font-size:16px;">缩略图上传</legend>
    </fieldset>
      <div class="layui-input-inline">
    <div class="layui-upload-drag" style="margin-left:10%;" id="upTinyPic">
      <i style="font-size:30px;" class="layui-icon"></i>
     <p style="font-size: 10px">点击上传，或将文件拖拽到此处</p>     
    </div>
      </div>
      <div class="layui-input-inline">
          <div  id="upTinyPicNow" style="margin-top: 20px;margin-left: 50px">
            <img src="${re.contextPath}/plugin/x-admin/images/bg.png" width="100px" height="100px" class="layui-upload-img">
          </div>

      </div>
    </div>
    <input type="hidden" id="hidBigPicUrl" name="bigPicUrl" value="" />
    <input type="hidden" id="hidTinyPicUrl" name="tinyPicUrl" value="" />
    <input type="hidden" id="hidDeviceRoadId" name="deviceRoadId" value="${topData.deviceRoadId}" />
    <input type="hidden" id="hidDeviceId" name="deviceId" value="${topData.deviceId}" />
    <input type="hidden" id="hidActionType" name="actionType" value="${topData.actionType}" />
    
    <div class="layui-form-item">
     <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
      <legend style="font-size:16px;">选择设备</legend>
    </fieldset>
    <div class="layui-inline" id="divDevice">
     <!--设备-->
     <label for="sltDeviceId" class="layui-form-label"><span class="x-red">*</span>设备</label>
      <div class="layui-input-inline">
       <select id="sltDeviceId" name="sltDeviceId" lay-verify="required" lay-ignore>
     <option value="">直接选择或搜索选择</option>
  	<#list deviceList as device>
          <option value="${device.id}">${device.shortName}</option>
    </#list>
    </select>
      </div>
    </div>
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
	  $('#sltDeviceId').select2();
     var actionType=$('#hidActionType').val();
     if(actionType==2)
    	 {
    	 $('#divDevice').hide();
    	 }
  });
  
  layui.use(['form','layer','upload'], function(){
    $ = layui.jquery;
    var form = layui.form,layer = layui.layer,upload = layui.upload;
    upload.render({
      elem: '#upBigPic',
      url: 'uploadBigPic',
      before: function(obj){
        //预读，不支持ie8
        obj.preview(function(index, headPicUrl, result){
          $('#upBigPicNow').find('img').remove();
          $('#upBigPicNow').append('<img src="'+ result +'" alt="'+ headPicUrl.name +'" width="100px" height="100px" class="layui-upload-img>');
        });
      },
      done: function(res){
       if(!res.flag){
         layer.msg(res.msg,{icon: 5});
       }
       else{
    	   $('#hidBigPicUrl').val(res.data.file_url);
       }
      }
    });
    
    upload.render({
        elem: '#upTinyPic',
        url: 'uploadTinyPic',
        before: function(obj){
          //预读，不支持ie8
          obj.preview(function(index, headPicUrl, result){
            $('#upTinyPicNow').find('img').remove();
            $('#upTinyPicNow').append('<img src="'+ result +'" alt="'+ headPicUrl.name +'" width="100px" height="100px" class="layui-upload-img>');
          });
        },
        done: function(res){
         if(!res.flag){
           layer.msg(res.msg,{icon: 5});
         }
         else{
      	   $('#hidTinyPicUrl').val(res.data.file_url);
         }
        }
      });

   $('#close').click(function(){
     var index = parent.layer.getFrameIndex(window.name);
     parent.layer.close(index);
   });
    //监听提交
    form.on('submit(add)', function(data){
      data.field.deviceId=$("#sltDeviceId").val();
      $.ajax({
        url:'addAdsTop',
        type:'post',
        contentType : 'application/json',  
        data:JSON.stringify(data.field),
        async:false,
        traditional: true,
        success:function(d){
          if(d.result_code==0){
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
            window.parent.layui.table.reload('roadList');
            window.top.layer.msg(d.result_msg,{icon:6,time:1000});
          }else{
            layer.msg(d.result_msg,{icon:5,time:1000});
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
