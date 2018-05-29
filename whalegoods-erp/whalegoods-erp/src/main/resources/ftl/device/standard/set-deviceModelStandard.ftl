<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>设置型号标准</title>
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
         <div class="layui-input-inline">
              <label  for="sltCtn" class="layui-form-label"><span class="x-red">*</span>柜数</label>
          <select id="sltCtn" name="sltCtn" lay-verify="required" lay-ignore>
        <option value="1" selected="">1</option>
        <option value="2" >2</option>
        <option value="3" >3</option>
      </select>
  </div>
    </div>
    <div class="layui-form-item">
         <div class="layui-input-inline">
              <label  for="sltFloor" class="layui-form-label"><span class="x-red">*</span>层数</label>
          <select id="sltFloor" name="sltFloor" lay-verify="required" lay-ignore>
        <option value="1" selected="">1</option>
        <option value="2" >2</option>
        <option value="3" >3</option>
        <option value="4" >4</option>
        <option value="5" >5</option>
        <option value="6" >6</option>
        <option value="7" >7</option>
        <option value="8" >8</option>
      </select>
  </div>
    </div>
    <div id="divPathNum" class="layui-form-item">
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
		 $('#sltCtn').select2();
		 $('#sltFloor').select2();
		 $('#sltDeviceModel').select2();
		 $('#sltCtn + span').css('width','50px');
		 $('#sltFloor + span').css('width','50px');
		 setPathNum();
		 $('#sltCtn').change(function(){
			 setPathNum();
		 });
		 $('#sltFloor').change(function(){
			 setPathNum();
		 });
		 
	  });
  
  function setPathNum(){
		 $('#divPathNum').find('lable').remove();
		 $('#divPathNum').find('div').remove();
		 var ctn=parseInt($('#sltCtn').val());
		 var floor=parseInt($('#sltFloor').val());
		 var j=1,i=1;
		 for(i=1;i<=ctn;i++){
			 for(j=1;j<=floor;j++){
				 $('#divPathNum').append("<div class='layui-form-item'><label style='width: 152px;' for='pathNum"+i+j+"' class='layui-form-label'><span class='x-red'>*</span>"+i+"柜"+j+"层最大货道数</label><div class='layui-input-inline'><input type='text'  id='pathNum"+i+j+"' name='pathNum'  lay-verify='required|number|ZZS' autocomplete='off' class='layui-input'></div></div>");
			 } 
		 }
  }
  
  layui.use(['form','layer'], function(){
	    $ = layui.jquery;
	    var form = layui.form,layer = layui.layer;
	    //自定义验证规则
	    form.verify({
	    	ZZS: function(value){
	        if(!/^[0-9]+$/.test(value)){
	          return "货道数必须为正整数";
	        }
	    	}
	    });
   $('#close').click(function(){
     var index = parent.layer.getFrameIndex(window.name);
     parent.layer.close(index);
   });
    //监听提交
    form.on('submit(confirm)', function(data){
    	var deviceModelId=$('#sltDeviceModel').val();
		 var ctn=parseInt($('#sltCtn').val());
		 var floor=parseInt($('#sltFloor').val());
    	var standard=[];
    	var obj=null;
		 for(var i=1;i<=ctn;i++){
			 for(var j=1;j<=floor;j++){
				 obj=new Object();
				 obj.ctn=i;
				 obj.floor=j;
				 obj.deviceModelId=deviceModelId;
				 obj.pathNum=parseInt($('#pathNum'+i+j).val());
				 standard.push(obj);
			 } 
		 }
      $.ajax({
        url:'setDeviceModelStandard',
        type:'post',
        contentType : 'application/json',  
        data:JSON.stringify(standard),
        async:false,
        traditional: true,
        success:function(d){
          if(d.result_code==0){
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
            window.parent.layui.table.reload('deviceModelStandardList');
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
