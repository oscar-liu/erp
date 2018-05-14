<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>货道信息列表</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
  <link rel="stylesheet" href="${re.contextPath}/plugin/layui/css/layui.css">
  <link rel="stylesheet" href="${re.contextPath}/plugin/erp/main.css">
  <link rel="stylesheet" href="${re.contextPath}/plugin/select2/css/select2.css">
  <script type="text/javascript" src="${re.contextPath}/plugin/jquery/jquery-3.2.1.min.js"></script>
  <script type="text/javascript" src="${re.contextPath}/plugin/select2/js/select2.min.js"></script>
  <script type="text/javascript" src="${re.contextPath}/plugin/qrcode/jquery.qrcode.min.js"></script>
  <script type="text/javascript" src="${re.contextPath}/plugin/select2/js/zh-CN.js"></script>
  <script type="text/javascript" src="${re.contextPath}/plugin/layui/layui.all.js" charset="utf-8"></script>
</head>

<body>
<div class="erp-search">
  <div class="select">
    设备：
   <div class="layui-inline">
       <div class="layui-input-inline">
     <select id="sltDeviceList" name="sltDeviceList" >
     <option value="">直接选择或搜索选择</option>
  	<#list deviceList as device>
          <option value="${device.id}">${device.shortName}</option>
    </#list>
    </select>
   </div>
   </div>
    <button class="select-on layui-btn layui-btn-sm layui-btn-primary" data-type="select"><i class="layui-icon">&#xe615;</i>查询</button>
    <@shiro.hasPermission name="device:road:add"><button class="layui-btn layui-btn-normal layui-btn-sm" data-type="add"><i class="layui-icon">&#xe608;</i>新增</button></@shiro.hasPermission>
    <@shiro.hasPermission name="device:road:update"><button class="layui-btn  layui-btn-sm" data-type="update"><i class="layui-icon">&#xe642;</i>编辑</button></@shiro.hasPermission>
    &nbsp;&nbsp;&nbsp;
    <@shiro.hasPermission name="device:road:adsmiddle"><button class="layui-btn layui-btn-warm layui-btn-sm" data-type="adsmiddle"><i class="layui-icon">&#xe664;</i>设置促销</button></@shiro.hasPermission>
    <@shiro.hasPermission name="device:road:adstop">
     <button class="layui-btn layui-btn-warm layui-btn-sm" data-type="adstop"><i class="layui-icon">&#xe6af;</i>设置广告</button>&nbsp;
      广告类型：
       <div class="layui-input-inline">
     <select id="sltTop" name="sltTop" lay-ignore>
        <option value="1" selected="">纯广告展示</option>
        <option value="2" >可购买商品</option>
      </select>
  </div>
    </@shiro.hasPermission>
     &nbsp;&nbsp;&nbsp;
    <@shiro.hasPermission name="device:road:prepay"><button class="layui-btn layui-btn-sm layui-btn-normal" data-type="prepay"><i class="layui-icon">&#xe65e;</i>生成支付二维码</button>&nbsp;
          支付类型：
       <div class="layui-input-inline">
     <select id="sltPayType" name="sltPayType" lay-ignore>
        <option value="1" selected="">微信</option>
        <option value="2" >支付宝</option>
      </select>
  </div>
    </@shiro.hasPermission>
    <button class="layui-btn layui-btn-sm icon-position-button" id="refresh" style="float: right;" data-type="reload"><i class="layui-icon">&#x1002;</i></button>
  </div>
</div>
<table id="roadList" class="layui-hide" lay-filter="road"></table>
<script type="text/html" id="rightToolBar">
<@shiro.hasPermission name="device:road:del"><a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a></@shiro.hasPermission>
</script>
<script type="text/html" id="tpllackLevel">
  {{#  if(d.stock<=d.warningNum){ }}
    <span style="color:red;">缺货</span>
  {{#  } else { }}
<span style="color:#76EE00;font-weight:bold;">充足</span>
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
  $(function(){
	  $('#sltDeviceList').select2();
	  $('#sltTop').select2({
		  minimumResultsForSearch: -1
	  });
	  $('#sltPayWay').select2({
		  minimumResultsForSearch: -1
	  });
  });
  
  layui.use('table', function () {
    var table = layui.table;
    table.render({
      id: 'roadList',
      elem: '#roadList', 
      url: 'showRoadList',
      cols: [[
        {checkbox: true, fixed: true},
        {field: 'shortName', title: '点位短名', align:'center'},
        {field: 'ctn', title: '柜号', align:'center'},
        {field: 'floor', title: '层级', align:'center'},
        {field: 'pathCode', title: '货道号', align:'center'},        
        {field: 'goodsName', title: '商品名称', align:'center'},
        {field: 'salePrice', title: '售价', align:'center'},
        {field: 'goodsCode',title: '商品编号',align:'center'},
        {field: 'lackLevel', title: '缺货紧急度', align:'center',templet: '#tpllackLevel'},
        {field: 'stock', title: '库存', align:'center',sort: true},        
        {field: 'deviceIdJp',title: '设备编号(鲸品)',align:'center'}, 
        {field: 'deviceIdSupp', title: '设备编号(供应商)', align:'center'},
        {field: 'capacity', title: '最大容量', align:'center',sort: true},
        {field: 'warningNum', title: '报警临界值', align:'center',sort: true},
        {field: 'deviceId',minWidth:0,width:0,type:'space',style:'display:none'},
        {field: 'right', title: '操作',align:'center', toolbar: "#rightToolBar"}
      ]],
      page: true,
      height: 'full-83'
    });

    var $ = layui.$, active = {
      select: function () {
        var deviceId = $('#sltDeviceList').val();
        table.reload('roadList', {
          where: {
        	  deviceId: deviceId
          }
        });
      },
      reload:function(){       
       table.reload('roadList', {
          where: {
        	  deviceId: null
          }
        });
       $("#sltDeviceList").find("option[value = '']").attr("selected","selected");
       $("#select2-sltDeviceList-container").text($("#sltDeviceList").find("option[value = '']").text());
      },
      add: function () {
        add('添加货道', 'showAddRoad', 800, 500);
      },
      update: function () {
        var checkStatus = table.checkStatus('roadList'), data = checkStatus.data;
        if (data.length != 1) {
          layer.msg('请选择一行编辑,已选['+data.length+']行', {icon: 5,time:1000});
          return false;
        }
        update('编辑货道', 'showUpdateRoad?id=' + data[0].id, 900, 500);
      },
      adsmiddle: function () {
          var checkStatus = table.checkStatus('roadList'), data = checkStatus.data;
          if (data.length != 9) {
            layer.msg('请选择9个货道,已选['+data.length+']个', {icon: 5,time:1000});
            return false;
          }
          var f=data[0].deviceIdJp;
          var arr =[];
          for(var i  in data){
        	  if(f!=data[i].deviceIdJp){
        		  layer.msg('所选货道必须属于同一个设备', {icon: 5,time:1000});
        		  return false;
        	  }
          }
          var dataObjArr=[];
          var dataObj=null;
          for(var j in data){
        	  dataObj=new Object();
        	  dataObj.id=data[j].id;
        	  dataObj.goodsName=data[j].goodsName;
        	  dataObjArr.push(dataObj);
          }
          var middleData=encodeURIComponent(JSON.stringify(dataObjArr));
          adsmiddle('设置促销', 'showAddAdsMiddle?middleData='+middleData , 800, 500);
        },
        adstop: function () {
        	var actionType = $("#sltTop").val();
        	var deviceId,deviceRoadId='';
        	if(actionType==2)
        		{
        		 var checkStatus = table.checkStatus('roadList'), data = checkStatus.data;
                 if (data.length != 1) {
                   layer.msg('请选择1一个货道,已选['+data.length+']个', {icon: 5,time:1000});
                   return false;
                 }
                 else{
                	 deviceRoadId=data[0].id;
                	 deviceId=data[0].deviceId;
                 }
        		}
        	adstop('设置广告', 'showAddAdsTop?actionType=' + actionType+'&deviceId='+deviceId+'&deviceRoadId='+deviceRoadId, 800, 500);
          },
          prepay: function () {
          	var checkStatus = table.checkStatus('roadList'), data = checkStatus.data;
            if (data.length != 1) {
              layer.msg('请选择1一个货道,已选['+data.length+']个', {icon: 5,time:1000});
              return false;
            }
            else{
             var device_code_sup=data[0].deviceIdSupp;
             var device_code_wg=data[0].deviceIdJp;
             var ctn=data[0].ctn;
             var floor=data[0].floor;
             var path_code=data[0].pathCode;
             var sale_type=$("#sltPayType").val();
            }
            prepay('生成支付二维码', 'createPrepayBack?saleType=' + sale_type+'&pathCode='+path_code+'&floor='+floor+'&ctn='+ctn+'&device_code_wg='+device_code_wg+'&device_code_sup='+device_code_sup, 400, 280);
            }
    };
    //监听工具条
    table.on('tool(road)', function (obj) {
      var data = obj.data;
      if (obj.event === 'del') {
        layer.confirm('确定删除该货道?',{ title:'提示'},
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
      url:"delRoad",
      type:"post",
      data:{id:id},
      async:false,
      success:function(d){
        if(d.result_code==0){
          window.top.layer.msg(d.result_msg,{icon:6,time:1000});
          layui.table.reload('roadList');
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
      id: 'road-update',
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
      id: 'road-add',
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
  
  function adsmiddle(title, url, w, h) {
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
		      id: 'road-adsmiddle',
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
  
  function adstop(title, url, w, h) {
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
	      id: 'road-adstop',
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
  
  function prepay(title, url, w, h) {
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
	      id: 'road-prepay',
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
