<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>添加内部购买登记</title>
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
  <!-- 商品-->
    <div class="layui-form-item">
     <div class="layui-inline">
     <label for="sltGoodsCode" class="layui-form-label"><span class="x-red">*</span>商品</label>
      <div class="layui-input-inline">
       <select id="sltGoodsCode" name="sltGoodsCode" lay-verify="required"  lay-ignore>
     <option value="">直接选择或搜索选择</option>
  	<#list goodsList as goods>
          <option value="${goods.id}">${goods.goodsName}</option>
    </#list>
    </select>
      </div>
    </div>
    </div>
    <!--所属入库批次 -->
    <div class="layui-form-item">
     <div class="layui-inline">
     <label for="sltGoodsStorageIn" class="layui-form-label"><span class="x-red">*</span>入库批次</label>
      <div class="layui-input-inline">
       <select id="sltGoodsStorageIn" name="sltGoodsStorageIn" lay-verify="required"  lay-ignore>
     <option value="">请先选择一个商品</option>
    </select>
    </div>
     </div>
    </div>  
     <!-- 购买数量、售价-->
    <div class="layui-form-item">
    <div class="layui-inline">
     <label for="iptSaleCount" class="layui-form-label"><span class="x-red">*</span>数量（个）</label>
      <div class="layui-input-inline"><input type="text"  id="iptSaleCount" name="saleCount"  lay-verify="required|number|ZZS"  autocomplete="off" class="layui-input"></div>
    </div>
     <div class="layui-inline">
     <label for="iptSalePrice" class="layui-form-label"><span class="x-red">*</span>售价</label>
      <div class="layui-input-inline"><input type="text"  id="iptSalePrice" name="salePrice"  lay-verify="required|number"  autocomplete="off" class="layui-input"></div>
    </div>
    </div>
    <!-- 购买登记日期 -->
    <div class="layui-form-item">
     <div class="layui-inline">
     <label for="iptSaleDate" class="layui-form-label"><span class="x-red">*</span>购买日期</label>
      <div class="layui-input-inline"><input type="text"  id="iptSaleDate" name="saleDate" placeholder="年-月-日" lay-verify="required|date"  autocomplete="off" class="layui-input"></div>
    </div>
    </div>
    <!-- 备注 -->
    <div class="layui-form-item layui-form-text">
      <label for="txtRemark" class="layui-form-label">备注</label>
      <div class="layui-input-block">
      <textarea placeholder="此处选填" class="layui-textarea" id="txtRemark" name="remark" ></textarea>
    </div>
    </div>
  <div style="width: 100%;height: 55px;background-color: white;border-top:1px solid #e6e6e6; position: fixed;bottom: 1px;margin-left:-20px;">
    <div class="layui-form-item" style=" float: right;margin-right: 30px;margin-top: 8px">
      <button  class="layui-btn layui-btn-normal" lay-filter="add" lay-submit=""> 确定</button>
      <button  class="layui-btn layui-btn-primary" id="close">取消</button>
    </div>
  </div>
  </form>
</div>
<script>
  $(function(){
	  $('#sltGoodsCode').select2();
	  $('#sltGoodsStorageIn').select2();
	  $('#sltGoodsStorageIn + span').css('width','300px');
	  $('#sltGoodsCode').change(function(){ 
	      $.ajax({
	          url:'getStorageInListByGoodsSkuId?goodsSkuId='+$("#sltGoodsCode").val()+'&currCountFlag=1',
	          type:'get',
	          async:false,
	          success:function(d){
	            if(d.result_code==0){
	            	$('#sltGoodsStorageIn').empty();
	            	$("#sltGoodsStorageIn").prepend("<option value=''>直接选择或搜索选择</option>");
	            	var arr=d.data;
	            	for(var i=0;i<arr.length;i++){
	            		$("#sltGoodsStorageIn").append("<option value='"+arr[i].id+"'>"+arr[i].goodsStorageInName+"</option>");
	            		}
	            }else{
	              layer.msg(d.result_msg,{icon:5,time:1000});
	              $('#sltGoodsStorageIn').empty();
	              $("#sltGoodsStorageIn").prepend("<option value=''>请先选择一个商品</option>");
	            }},
	            error:function(){
	              var index = parent.layer.getFrameIndex(window.name);
	              parent.layer.close(index);
	              window.top.layer.msg('请求失败',{icon:5,time:1000});
	          }
	        });
		 });
  });
  layui.use(['form','layer'], function(){
    $ = layui.jquery;
    var form = layui.form,layer = layui.layer,laydate = layui.laydate;
    laydate.render({
        elem: '#iptSaleDate'
        ,max:0
      });
    form.verify({
      ZZS:function(value){
            if(!/^[0-9]+$/.test(value)){
              return "必须为正整数";
            }
        }
    });
   $('#close').click(function(){
     var index = parent.layer.getFrameIndex(window.name);
     parent.layer.close(index);
   });
    //监听提交
    form.on('submit(add)', function(data){
     data.field.goodsSkuId=$("#sltGoodsCode").val();
     data.field.goodsStorageInId=$("#sltGoodsStorageIn").val();
      $.ajax({
        url:'addGoodsStorageIr',
        type:'post',
       contentType : 'application/json',
        data:JSON.stringify(data.field),
        async:false,
        traditional: true,
        success:function(d){
          if(d.result_code==0){
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
            window.parent.layui.table.reload('storageIrList');
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
