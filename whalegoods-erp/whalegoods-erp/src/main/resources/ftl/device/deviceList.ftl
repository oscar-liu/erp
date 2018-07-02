<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>设备列表</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
  <link rel="stylesheet" href="${re.contextPath}/plugin/layui/css/layui.css">
  <link rel="stylesheet" href="${re.contextPath}/plugin/erp/main.css">
  <script type="text/javascript" src="${re.contextPath}/plugin/jquery/jquery-3.2.1.min.js"></script>
  <script type="text/javascript" src="${re.contextPath}/plugin/layui/layui.all.js" charset="utf-8"></script>
  	<style type="text/css">
	body, html,#allmap {width: 100%;height: 100%;overflow: hidden;margin:0;font-family:"微软雅黑";}
	</style>
  <script type="text/javascript" src="http://api.map.baidu.com/api?v=3.0&ak=YSGitlzU7AI58VMnjYQgQEOQcHOFcL2x"></script>
</head>

<body>
<div class="erp-search">
  <div class="select">
           点位短名： <div class="layui-inline"><input class="layui-input" height="20px" id="shortName" autocomplete="off"></div>
    <button class="select-on layui-btn layui-btn-sm layui-btn-primary" data-type="select"><i class="layui-icon">&#xe615;</i>模糊查询</button>
    <@shiro.hasPermission name="device:add"> <button class="layui-btn layui-btn-normal layui-btn-sm" data-type="add"><i class="layui-icon">&#xe608;</i>新增</button></@shiro.hasPermission>
    <@shiro.hasPermission name="device:update"><button class="layui-btn  layui-btn-sm" data-type="update"><i class="layui-icon">&#xe642;</i>编辑</button></@shiro.hasPermission>
    <@shiro.hasPermission name="device:map"><button class="layui-btn  layui-btn-sm" data-type="map"><i class="layui-icon">&#xe715;</i>查看地图</button></@shiro.hasPermission>
    <button class="layui-btn layui-btn-sm icon-position-button" id="refresh" style="float: right;" data-type="reload"><i class="layui-icon">&#x1002;</i></button>
  </div>
</div>
<table id="deviceList" class="layui-hide" lay-filter="device"></table>
<script type="text/html" id="rightToolBar">
<@shiro.hasPermission name="device:del"><a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a></@shiro.hasPermission>
</script>
<script type="text/html" id="tplLockStatus">
<input type="checkbox" name="lockStatus" value="{{d.id}}" lay-skin="switch" lay-text="开启|锁定" lay-filter="radioLockStatus" {{ d.lockStatus == 2 ? 'checked' : '' }}>
</script>
<script type="text/html" id="tplDeviceStatus">
  {{#  if(d.deviceStatus==1||d.deviceStatus==2){ }}
<input type="checkbox" name="deviceStatus" value="{{d.id}}" lay-skin="switch" lay-text="运行|停止" lay-filter="radioDeviceStatus" {{ d.deviceStatus == 1 ? 'checked' : '' }}>
  {{#  } else { }}
<span style="color: #F581B1;">离线</span>
  {{#  } }}
</script>
<script>

  document.onkeydown = function (e) {
    var theEvent = window.event || e;
    var code = theEvent.keyCode || theEvent.which;
    if (code == 13) {
      $(".select .select-on").click();
    }
  }
  
  layui.use(['table','layer','form'], function () {
    var table = layui.table,form = layui.form,layer = layui.layer;
    table.render({
      id: 'deviceList',
      elem: '#deviceList', 
      url: 'showDeviceList',
      cols: [[
        {checkbox: true, fixed: true},
        {field: 'shortName',title: '点位短名',align:'center' }, 
        {field: 'location', title: '点位地址', align:'center'},
        {field: 'deviceStatus',title: '设备运行状态',align:'center',templet: '#tplDeviceStatus', unresize: true},
        {field: 'signCode', title: '签到码', align:'center'},
        {field: 'deviceIdJp', title: '设备编号（鲸品）', align:'center'},
        {field: 'deviceIdSupp', title: '设备编号（供应商）', align:'center'},
        {field: 'modelName', title: '设备型号', align:'center'},
        {field: 'lockStatus', title: '库存锁定状态',templet: '#tplLockStatus',align:'center'},
        {field: 'right', title: '操作',align:'center', toolbar: "#rightToolBar"}
      ]],
      page: true,
      height: 'full-46'
    });
    
    form.on('switch(radioDeviceStatus)', function(obj){
        $.ajax({
            url:"updateDeviceStatus",
            type:"post",
            data:{id:this.value,device_status:obj.elem.checked},
            async:false,
            success:function(r){
              if(r.result_code==0){
                window.top.layer.msg(r.result_msg,{icon:6,time:1000});
              }else{
                window.top.layer.msg(r.result_msg,{icon:5,time:1000});
              }
            },
            error:function(){
            	window.top.layer.msg("设置失败,请联系管理员",{icon:5,time:1000});
            }
          });
    });
    
    form.on('switch(radioLockStatus)', function(obj){
        $.ajax({
            url:"updateLockStatus",
            type:"post",
            data:{id:this.value,lock_status:obj.elem.checked},
            async:false,
            success:function(r){
              if(r.result_code==0){
                window.top.layer.msg(r.result_msg,{icon:6,time:1000});
              }else{
                window.top.layer.msg(r.result_msg,{icon:5,time:1000});
              }
            },
            error:function(){
            	window.top.layer.msg("设置失败,请联系管理员",{icon:5,time:1000});
            }
          });
    });

    var $ = layui.$, active = {
      select: function () {
        var shortName = $('#shortName').val();
        table.reload('deviceList', {
          where: {
        	  shortName: shortName,
          }
        });
      },
      reload:function(){
        $('#shortName').val('');
        table.reload('deviceList', {
          where: {
        	  shortName: null,
          }
        });
      },
      add: function () {
        add('添加设备（仅需填写供应商提供的编号）', 'showAddDevice', 400, 450);
      },
      update: function () {
        var checkStatus = table.checkStatus('deviceList'), data = checkStatus.data;
        if (data.length != 1) {
          layer.msg('请选择一行编辑,已选['+data.length+']行', {icon: 5,time:1000});
          return false;
        }
        update('更新设备', 'showUpdateDevice?id=' + data[0].id, 400, 450);
      },
      map: function () {
          var checkStatus = table.checkStatus('deviceList'), data = checkStatus.data;
          if (data.length == 0) {
            layer.msg('请选择至少一行查看', {icon: 5,time:1000});
            return false;
          }
          $('#deviceList').after("<div id='allmap' style='width:800px;height:500px;'></div>");
       // 百度地图API功能
      	var map = new BMap.Map("allmap");
      	// 创建地址解析器实例
      	var myGeo = new BMap.Geocoder();
      	var j,len;
      	for(j = 0,len=data.length; j < len; j++) {
      		// 将地址解析结果显示在地图上,并调整地图视野
      		myGeo.getPoint(data[j].location, function(point){
      			if (point) {
      				map.centerAndZoom(point, 12);
      	          var marker = new BMap.Marker(point);  // 创建标注
      		map.addOverlay(marker);               // 将标注添加到地图中
      		marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
      	  map.enableScrollWheelZoom(true);
      			}else{
      				alert("您选择地址没有解析到结果!");
      			}
      		});
      	}
          layer.open({
        	  type: 1,
        	  title: '查看地图',
        	  skin: 'layui-layer-nobg', //没有背景色
        	  shadeClose: false,
        	  area: ['801px', '500px'],
        	  closeBtn: 0,
        	  content: $('#allmap'),
        	  btn: ['关闭'],
        	  yes: function(index, layero){
        		  $('#allmap').remove();
        	      $("[id^='layui-layer']").remove();
        		  layer.close(index);
        		  }
        	});
        }
    };
    //监听工具条
    table.on('tool(device)', function (obj) {
      var data = obj.data;
      if (obj.event === 'del') {
        layer.confirm('确定删除：[<label style="color: #00AA91;">' + data.shortName + '</label>]的设备?',{ title:'提示'},
         function (index) {
            del(data.id);
            layer.close(index);
        });
      } 
    });

    $('.select .layui-btn').on('click', function () {
      var type = $(this).data('type');
      active[type] ? active[type].call(this) : '';
    });

  });

  function del(id) {
    $.ajax({
      url:"delDevice",
      type:"post",
      data:{id:id},
      async:false,
      success:function(d){
        if(d.result_code==0){
          window.top.layer.msg(d.result_msg,{icon:6,time:1000});
          layui.table.reload('deviceList');
        }else{ 
          window.top.layer.msg(d.result_msg);
        }},
        error:function(){
          window.top.layer.msg("删除失败,请联系管理员",{icon:5,time:1000});
      }
    });
  }

  function update(title, url, w, h) {
    if (title == null || title == '') {
      title = false;
    }
    if (url == null || url == '') {
      url = "404.html";
    }
    if (w == null || w == '') {
      w = ($(window).width() * 0.9);
    }
    if (h == null || h == '') {
      h = ($(window).height() - 50);
    }
    layer.open({
      id: 'device-update',
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

  /*弹出层*/
  /*
   参数解释：
   title   标题
   url     请求的url
   id      需要操作的数据id
   w       弹出层宽度（缺省调默认值）
   h       弹出层高度（缺省调默认值）
   */
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
      id: 'device-add',
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
