<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>添加货道</title>
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
      <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;"><legend style="font-size:16px;">货道信息</legend></fieldset>
    </div>
    <div class="layui-form-item">
    <div class="layui-inline">
    <!-- 鲸品设备编号-->
    <label for="deviceIdJp" class="layui-form-label"><span class="x-red">*</span>鲸品设备编号</label>
      <div class="layui-input-inline"><input type="text"  id="deviceIdJp" name="deviceIdJp"  lay-verify="required" autocomplete="off" class="layui-input"></div>
    </div>
    <div class="layui-inline">
    <!--供应商设备编号-->
      <label for="deviceIdSupp" class="layui-form-label"><span class="x-red">*</span>供应商设备编号</label>
      <div class="layui-input-inline"><input type="text"  id="deviceIdSupp" name="deviceIdSupp" lay-verify="required"  autocomplete="off" class="layui-input"></div>
       </div>
    </div>
         
    <div class="layui-form-item">
     <div class="layui-inline">
     <!--货道柜号-->
     <label for="ctn" class="layui-form-label"><span class="x-red">*</span>货道柜号</label>
      <div class="layui-input-inline"><input type="text"  id="ctn" name="ctn" lay-verify="required|number|ZZS" autocomplete="off" class="layui-input"></div>
     </div>
     <div class="layui-inline">
     <!--层级-->
     <label for="floor" class="layui-form-label"><span class="x-red">*</span>层级</label>
      <div class="layui-input-inline"><input type="text"  id="floor" name="floor" lay-verify="required|number|ZZS"  autocomplete="off" class="layui-input"></div>
     </div>
    </div>
     
    <div class="layui-form-item">
    <div class="layui-inline">
    <!--货道编号-->
    <label for="pathCode" class="layui-form-label"><span class="x-red">*</span>货道编号</label>
      <div class="layui-input-inline"><input type="text"  id="pathCode" name="pathCode" lay-verify="required|number|ZZS"   autocomplete="off" class="layui-input"></div>
     </div>
     <div class="layui-inline">
     <!--商品编号-->
     <label for="goodsCode" class="layui-form-label"><span class="x-red">*</span>商品编号</label>
      <div class="layui-input-inline"><input type="text"  id="goodsCode" name="goodsCode" lay-verify="required"  autocomplete="off" class="layui-input"></div>
     </div>
      
    </div>
 
    <div class="layui-form-item">
    <div class="layui-inline">
     <!--售价-->
     <label for="salePrice" class="layui-form-label"><span class="x-red">*</span>售价</label>
      <div class="layui-input-inline"><input type="text"  id="salePrice" name="salePrice"  lay-verify="required|number"  autocomplete="off" class="layui-input"></div>
    </div>
    <div class="layui-inline">
     <!--货道容量-->
     <label for="capacity" class="layui-form-label"><span class="x-red">*</span>货道容量</label>
      <div class="layui-input-inline"><input type="text"  id="capacity" name="capacity" lay-verify="required|number|ZZS"  autocomplete="off" class="layui-input"></div>
    </div>
     
    </div>
    
    <!-- 报警临界值-->
    <div class="layui-form-item">
      <label for="warningNum" class="layui-form-label"><span class="x-red">*</span>报警临界值</label>
      <div class="layui-input-inline"><input type="text"  id="warningNum" name="warningNum" lay-verify="required|number|ZZS"  autocomplete="off" class="layui-input"></div>
      <div id="ms" class="layui-form-mid layui-word-aux"><span class="x-red">*</span><span id="ums">临界值不能大于或等于货道容量值</span></div>
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
      $('#warningNum').on("change",function(){
        var capacity=$('#capacity').val();
        var warningNum=$('#warningNum').val();
        $('#ms').find('span').text();
        if(warningNum>=capacity)
        	{        	
        	$('#ms').find('span').css('color','red').text('临界值不能大于或等于货道容量值');
        	}
        else{
        	$('#ms').find('span').css('color','green').text('临界值可用');
        }
      });
  });
  
  layui.use(['form','layer'], function(){
    $ = layui.jquery;
    var form = layui.form,layer = layui.layer;
    
    //自定义验证规则
    form.verify({
    	ZZS: function(value){
        if(!/^[0-9]+$/.test(value)){
          return "货道容量必须为正整数";
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
        url:'addRoad',
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
