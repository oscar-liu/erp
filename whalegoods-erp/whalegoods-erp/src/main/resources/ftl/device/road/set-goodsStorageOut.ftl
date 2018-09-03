<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>设置商品出库批次</title>
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
   <input value="${road.id}" type="hidden" name="id" >
     <div class="layui-form-item">
       <!-- 出库批次 -->
     <label for="sltGoodsStorageOut" class="layui-form-label"><span class="x-red">*</span>出库批次</label>
      <div class="layui-input-inline">
       <select id="sltGoodsStorageOut" name="sltGoodsStorageOut" lay-verify="required" lay-ignore>
     <option value="">直接选择或搜索选择</option>
  	<#list goodsStorageOutList as storageOut>
          <option value="${storageOut.id}">${storageOut.goodsStorageOutName}</option>
    </#list>
    </select>
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
$(function(){
	$('#sltGoodsStorageOut').select2();
	$("#select2-sltGoodsStorageOut-container").text($("#sltGoodsStorageOut").find("option[value = "+'${road.goodsStorageOutId}'+"]").text());
	$('#sltGoodsStorageOut').val('${road.goodsStorageOutId}');
	$('#sltGoodsStorageOut + span').css('width','300px');
});

layui.use(['form','layer'], function(){
  $ = layui.jquery;
  var form = layui.form,layer = layui.layer;

 $('#close').click(function(){
   var index = parent.layer.getFrameIndex(window.name);
   parent.layer.close(index);
 });
  //监听提交
  form.on('submit(confirm)', function(data){
	  data.field.goodsStorageOutId=$("#sltGoodsStorageOut").val();
    $.ajax({
      url:'updateGoodsStorageOut',
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
