<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>添加商品返仓</title>
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
    <!-- 设备-->
    <div class="layui-form-item">
     <div class="layui-inline">
     <label for="sltDeviceId" class="layui-form-label"><span class="x-red">*</span>设备</label>
      <div class="layui-input-inline">
       <select id="sltDeviceId" name="sltDeviceId" lay-verify="required"  lay-ignore>
     <option value="">直接选择或搜索选择</option>
  	<#list deviceList as device>
          <option value="${device.id}">${device.shortName}</option>
    </#list>
    </select>
      </div>
    </div>
    </div>
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
        
     <!-- 返仓数量 -->
    <div class="layui-form-item">
    <div class="layui-inline">
     <label for="iptRtwNum" class="layui-form-label"><span class="x-red">*</span>数量（个）</label>
      <div class="layui-input-inline"><input type="text"  id="iptRtwNum" name="iptRtwNum"  lay-verify="required|number|ZZS"  autocomplete="off" class="layui-input"></div>
    </div>
    <div class="layui-inline">
     <label for="iptRdDay" class="layui-form-label"><span class="x-red">*</span>返仓日期</label>
      <div class="layui-input-inline"><input type="text"  id="iptRdDay" name="rdDay" placeholder="年-月-日" lay-verify="required|date"  autocomplete="off" class="layui-input"></div>
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
	  $('#sltDeviceId').select2();
	  $('#sltGoodsStorageIn + span').css('width','300px');
	  $('#sltGoodsCode').change(function(){
	      $.ajax({
	          url:'getStorageInListByGoodsSkuId?goodsSkuId='+$("#sltGoodsCode").val(),
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
        elem: '#iptRdDay'
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
     layer.confirm('想要继续添加返仓记录吗?',{ title:'提示'},
    	        function (index) {
    	 add('商品返仓', 'showAddGoodsStorageRtw', 700,500);
    	 layer.close(index);
    	        },function(index){
    	        	addGoodsStorageRtw(data);
    	        	layer.close(index);
    	        	return false;
    	        });
      return false;
    });
    form.render();
  });
  
  function addGoodsStorageRtw(data){
      $.ajax({
          url:'addGoodsStorageRtw',
          type:'post',
         contentType : 'application/json',
          data:JSON.stringify(data.field),
          async:false,
          traditional: true,
          success:function(d){
            if(d.result_code==0){
              var index = parent.layer.getFrameIndex(window.name);
              parent.layer.close(index);
              window.parent.layui.table.reload('storageRtwList');
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
  }
  
  function add(title, url, w, h) {
	    if (title == null || title == '') {
	      title = false;
	    }
	    ;
	    if (url == null || url == '') {
	      url = "404.html";
	    }
	    ;
	    if (w == null || w == '') {
	      w = ($(window).width() * 0.9);
	    }
	    ;
	    if (h == null || h == '') {
	      h = ($(window).height() - 50);
	    }
	    ;
	    layer.open({
	      id: 'storageRtw-add',
	      type: 2,
	      area: [w + 'px', h + 'px'],
	      fix: false,
	      maxmin: true,
	      shadeClose: false,
	      shade: 0.4,
	      title: title,
	      content: url
	    });
	  }
</script>
</body>

</html>
