<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>编辑促销商品</title>
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
      <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;"><legend style="font-size:16px;">促销信息</legend></fieldset>
    </div>
     <input value="${adsMiddle.id}" type="hidden" name="id">
     <input value="${adsMiddle.deviceId}" type="hidden" name="deviceId">
    <div class="layui-form-item">
     <div class="layui-inline">
        <!--商品编号-->
     <label for="sltGoodsCode" class="layui-form-label"><span class="x-red">*</span>商品</label>
      <div class="layui-input-inline">
       <select id="sltGoodsCode" name="sltGoodsCode" lay-verify="required"  lay-ignore>
     <option value="">直接选择或搜索选择</option>
  	<#list goodsList as goods>
          <option value="${goods.goodsCode}">${goods.goodsName}</option>
    </#list>
    </select>
      </div>
    </div>
    </div>
    
     <!-- 时间类型-->
    <div class="layui-form-item">
         <div class="layui-input-inline">
              <label for="sltTimeType" class="layui-form-label"><span class="x-red">*</span>时间类型</label>
          <select id="sltTimeType" name="sltTimeType" lay-verify="required" lay-ignore>
        <option value="1" selected="">整点</option>
        <!-- <option value="2" >时间段</option> -->
      </select>
  </div>
    </div>
    
         <!-- 整点-->
    <div class="layui-form-item" id="divHmsRange">
         <div class="layui-inline">
              <label for="iptHmsRange" class="layui-form-label"><span class="x-red">*</span>时间范围</label>
              <div class="layui-input-inline"><input type="text" class="layui-input"  id="iptHmsRange" placeholder="开始 到 结束"></div>
  </div>
    </div>
             <!-- 时间段-->
    <div class="layui-form-item" id="divDateRange">
         <div class="layui-inline">
              <label for="iptDateRange" class="layui-form-label"><span class="x-red">*</span>时间范围</label>
               <div class="layui-input-inline"><input type="text" class="layui-input"  id="iptDateRange" placeholder="开始 到 结束" style="width:292px;"></div>
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
  var flag,msg;
  $(function(){
	 $('#sltGoodsCode').select2();
	 $('#sltTimeType').select2();
	 $('#sltTimeType + span').css('width','80px');
	 
	 $("#select2-sltGoodsCode-container").text($("#sltGoodsCode").find("option[value = "+'${adsMiddle.goodsCode}'+"]").text());
	 $('#sltGoodsCode').val('${adsMiddle.goodsCode}');
	 $("#select2-sltTimeType-container").text($("#sltTimeType").find("option[value = "+'${adsMiddle.type}'+"]").text());
	 $('#sltTimeType').val('${adsMiddle.type}');
	 
	 if($('#sltTimeType').val()==1){
		 $('#iptHmsRange').val('${adsMiddle.timeRange}');
		 $('#divDateRange').hide();
	 }
	 if($('#sltTimeType').val()==2){
		 $('#iptDateRange').val('${adsMiddle.timeRange}');
		 $('#divHmsRange').hide();
	 }
	 
	 $('#sltTimeType').on('change',function(){
		 var selectVal = $(this).find('option:selected').val();
		 if(selectVal==1){
			 $('#divHmsRange').show();
			 $('#divDateRange').hide();
		 }
		 if(selectVal==2){
			 $('#divHmsRange').hide();
			 $('#divDateRange').show();
		 }
		});
	 
  });
  
  layui.use(['form','layer','laydate'], function(){
    $ = layui.jquery;
    var form = layui.form,layer = layui.layer,laydate = layui.laydate;;
    
    laydate.render({
      elem: '#iptHmsRange'
      ,type: 'time'
      ,range: true
    });
    
    laydate.render({
      elem: '#iptDateRange'
      ,type:'datetime'
      ,range: true
    });

   $('#close').click(function(){
     var index = parent.layer.getFrameIndex(window.name);
     parent.layer.close(index);
   });
    //监听提交
    form.on('submit(confirm)', function(data){
     data.field.goodsCode=$("#sltGoodsCode").val();
     data.field.type=$("#sltTimeType").val();
     data.field.hmsRange=$("#iptHmsRange").val();
     data.field.dateRange=$("#iptDateRange").val();
      $.ajax({
        url:'updateAdsMiddle',
        type:'post',
       contentType : 'application/json',  
        data:JSON.stringify(data.field),
        async:false,
        traditional: true,
        success:function(d){
          if(d.result_code==0){
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
            window.parent.layui.table.reload('adsMiddleList');
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
