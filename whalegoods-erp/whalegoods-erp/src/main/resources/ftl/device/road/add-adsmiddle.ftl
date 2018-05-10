<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>设置中部促销栏商品</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi" />
  <link rel="stylesheet" href="${re.contextPath}/plugin/layui/css/layui.css">
  <script type="text/javascript" src="${re.contextPath}/plugin/jquery/jquery-3.2.1.min.js"></script>
  <script type="text/javascript" src="${re.contextPath}/plugin/jquery/jquery-ui.js"></script>
  <script type="text/javascript" src="${re.contextPath}/plugin/layui/layui.all.js" charset="utf-8"></script>
  <style>
	table
	{
		border-collapse:collapse;
	}
	table td
	{
		empty-cells : show;
		text-align:center;
	}
</style>
</head>

<body>
<div class="x-body">
 <form class="layui-form layui-form-pane" style="margin-left: 20px;">
    <div style="width:100%;height:400px;overflow: auto;"></div>
    <table>
<tr>
	<td>
		<table border = "1">
		<#list deviceRoad as deviceRoads>
                <tr height="40"><td id="draggable1" width = "200" bgcolor="lightgreen"><span id="${deviceRoad.id}">${deviceRoad.goodsName}</span></td></tr>
         </#list>
		</table>
	</td>
	<td>
		<table border = "1" >
		<tr height="40">
			<td width = "250">早上7点-9点</td>
			<td width = "250">中午12:00-14:00</td>
			<td width = "250">下午18:00-20:00</td>
		</tr>
		<tr height="40">
			<td width = "250" class="oneM" id="target11"></td>
			<td width = "250" class="twoM" id="target12"></td>
			<td width = "250" class="threeM" id="target13"></td>
		</tr>
		<tr height="40">
			<td width = "250" class="oneM" id="target21"></td>
			<td width = "250" class="twoM" id="target21"></td>
			<td width = "250" class="threeM" id="target21"></td>
		</tr>
		<tr height="40">
			<td width = "250" class="oneM" id="target31"></td>
			<td width = "250" class="twoM" id="target31"></td>
			<td width = "250" class="threeM" id="target31"></td>
		</tr>
		</table>
	</td>
</tr>
</table>
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
      $('td[id^="draggable"]').draggable({
		   helper:"clone",
		   cursor: "move"
		 });
	   $('td[id^="target"]').droppable({
			drop:function(event,ui){ 
				$(this).children().remove();
				var source = ui.draggable.clone();
				$('<img/>', {
					src: 'btn_delete.png',
					style:'display:none',
					click: function() {
					  source.remove();
					}
				}).appendTo(source);
				source.mouseenter(function () { 
					$(this).find("img").show();
				}); 
				source.mouseleave(function () {
					$(this).find("img").hide();
				}); 
				$(this).append(source);
			}
		});
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
      var oneM= $('.oneM > span');
      var twoM= $('.twoM > span');
      var threeM= $('.threeM > span');
      console.log(oneM);
      console.log(twoM);
      console.log(threeM);
      var oneData=[];
      var twoData=[];
      var threeData=[];
      for(var i in oneM){
    	  var newI={'deviceRoadId':i.attr('id')}; 
    	  oneData.push(newI);
      }
      for(var i in twoM){
    	  var newI={'deviceRoadId':i.attr('id')}; 
    	  twoData.push(newI);
      }
      for(var i in threeM){
    	  var newI={'deviceRoadId':i.attr('id')}; 
    	  threeData.push(newI);
      }
      data.field.one=oneData;
      data.field.two=twoData;
      data.field.three=threeData;
      $.ajax({
        url:'addAdsMiddle',
        type:'post',
       contentType : 'application/json',  
        data:,data.field
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
