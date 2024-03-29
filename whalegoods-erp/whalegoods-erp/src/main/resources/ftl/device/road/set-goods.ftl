<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>修改货道商品</title>
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
   <input value="${road.deviceId}" type="hidden" name="deviceId" >
     <div class="layui-form-item">
        <!--商品编号-->
     <label for="sltGoodsCode" class="layui-form-label"><span class="x-red">*</span>商品</label>
      <div class="layui-input-inline">
       <select id="sltGoodsCode" name="sltGoodsCode" lay-verify="required" lay-ignore>
     <option value="">直接选择或搜索选择</option>
  	<#list goodsList as goods>
          <option value="${goods.goodsCode}">${goods.goodsName}${goods.spec}</option>
    </#list>
    </select>
      </div>
    </div>
    <div class="layui-form-item">
     <!--售价-->
     <label for="salePrice" class="layui-form-label"><span class="x-red">*</span>售价</label>
      <div class="layui-input-inline"><input type="text"  id="salePrice" name="salePrice" value="${road.salePrice}" lay-verify="required|number|FFS"  autocomplete="off" class="layui-input"></div>
    </div>
    <!-- 建议销售价 -->
    <div class="layui-form-item">
    <div id="ms" class="layui-form-mid layui-word-aux"></div>
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
var flag,msg;
var lockStatus=$('#hidLockStatus').val();
$(function(){
	$('#sltGoodsCode').select2();
	$("#select2-sltGoodsCode-container").text($("#sltGoodsCode").find("option[value = "+'${road.goodsCode}'+"]").text());
	$('#sltGoodsCode').val('${road.goodsCode}');
	$('#sltGoodsCode').change(function(){
		$('#ms').find('span').remove();
	    $.ajax({
	        url:'getRecommendPrice?goodsCode='+$('#sltGoodsCode').val(),
	        type:'get',
	        async:false,
	        success:function(d){
	          if(d.result_code==0){
	        	  $('#ms').append("<span style='color: red;'>"+"参考销售价："+d.data+"</span>");
	          }else{
	            layer.msg(d.result_msg,{icon:5,time:1000});
	          }},
	          error:function(){
	            window.top.layer.msg('请求失败',{icon:5,time:1000});
	        }
	      });
	});
});

layui.use(['form','layer'], function(){
  $ = layui.jquery;
  var form = layui.form,layer = layui.layer;
  
  //自定义验证规则
  form.verify({
	  FFS: function(value){
	  var reg = /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/;
      if(!reg.test(value)){
        return "必须为非负数";
      }
  	}
  });

 $('#close').click(function(){
   var index = parent.layer.getFrameIndex(window.name);
   parent.layer.close(index);
 });
  //监听提交
  form.on('submit(confirm)', function(data){
	  data.field.goodsCode=$("#sltGoodsCode").val();
    $.ajax({
      url:'updateGoods',
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
