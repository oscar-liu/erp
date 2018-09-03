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
  <script type="text/javascript" src="${re.contextPath}/plugin/layui/layui.all.js" charset="utf-8"></script>
    <script type="text/javascript" src="${re.contextPath}/plugin/select2/js/select2.min.js"></script>
  <script type="text/javascript" src="${re.contextPath}/plugin/select2/js/zh-CN.js"></script>
  <script type="text/javascript" src="${re.contextPath}/plugin/qrcode/jquery.qrcode.min.js"></script>
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
   &nbsp;商品：
   <div class="layui-inline">
       <div class="layui-input-inline">
     <select id="sltGoodsList" name="sltGoodsList" >
     <option value="">直接选择或搜索选择</option>
  	<#list goodsList as goods>
          <option value="${goods.id}">${goods.goodsName}${goods.spec}</option>
    </#list>
    </select>
   </div>
   </div>
    <button class="select-on layui-btn layui-btn-sm layui-btn-primary" data-type="select"><i class="layui-icon">&#xe615;</i>查询</button>
    <@shiro.hasPermission name="device:road:add"><button class="layui-btn layui-btn-normal layui-btn-sm" data-type="add"><i class="layui-icon">&#xe608;</i>新增</button></@shiro.hasPermission>
    <@shiro.hasPermission name="device:road:update"><button class="layui-btn  layui-btn-sm" data-type="update"><i class="layui-icon">&#xe642;</i>编辑</button></@shiro.hasPermission>
    <@shiro.hasPermission name="device:road:excel"><button class="layui-btn  layui-btn-sm" data-type="excel"><i class="layui-icon">&#xe601;</i>导出货道清单</button></@shiro.hasPermission>
    <button class="layui-btn layui-btn-sm icon-position-button" id="refresh" style="float: right;" data-type="reload"><i class="layui-icon">&#x1002;</i></button>
  </div>
</div>
<div id="divSecond" class="layui-col-md12" style="height: 33px;margin-top: 4px;vertical-align:middle;border-bottom-width:-2;">
     <div class="select">
           <@shiro.hasPermission name="device:road:init"><button class="layui-btn layui-btn-normal layui-btn-sm" data-type="init"><i class="layui-icon">&#xe614;</i>初始化货道</button></@shiro.hasPermission>
      <@shiro.hasPermission name="device:road:prepay"><button class="layui-btn layui-btn-sm layui-btn-normal" data-type="prepay"><i class="layui-icon">&#xe65e;</i>生成支付二维码</button>&nbsp;
          支付类型：
       <div class="layui-inline">
       <div class="layui-input-inline">
     <select id="sltPayType" name="sltPayType" lay-ignore>
        <option value="1" selected="">微信</option>
        <option value="2" >支付宝</option>
      </select>
      </div>
  </div>
    </@shiro.hasPermission>
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
<script type="text/html" id="tplAdsMiddleType">
  {{#  if(d.adsMiddleType!=3){ }}
    <span>{{d.goodsName}}<span style="color:red;">（促销中）</span></span>
  {{#  } else { }}
<span>{{d.goodsName}}</span>
  {{#  } }}
</script>
<script type="text/html" id="tplGoodsStorageOut">
  {{#  if(d.goodsStorageOutName==null||d.goodsStorageOutName==undefined||d.goodsStorageOutName==""){ }}
    <span style="color: #F581B1;">无</span>
  {{#  } else { }}
<span>{{d.goodsStorageOutName}}</span>
  {{#  } }}
</script>
<script type="text/html" id="tplGoodsStorageIn">
  {{#  if(d.goodsStorageInName==null||d.goodsStorageInName==undefined||d.goodsStorageInName==""){ }}
    <span style="color: #F581B1;">无</span>
  {{#  } else { }}
<span>{{d.goodsStorageInName}}</span>
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
  if($('#divSecond').children().length==0){
	  var tableHeight='full-46';
	  $('#divSecond').remove();
	  }
  else {
	  var tableHeight='full-80';
  }
  
  $(function(){
	  $('#sltDeviceList').select2();
	  $('#sltGoodsList').select2();
	  $('#sltPayType').select2();
	  $('#sltPayType + span').css('width','80px');
  });
  
  layui.use('table', function () {
    var table = layui.table;
    table.render({
      id: 'roadList',
      elem: '#roadList', 
      url: 'showRoadList',
      cols: [[
        {checkbox: true, fixed: true},
        {field: 'shortName', title: '点位短名', align:'center',width:120},
        {field: 'ctn', title: '柜号', align:'center',width:60},
        {field: 'floor', title: '层级', align:'center',width:60},
        {field: 'pathCode', title: '货道号', align:'center',width:76},        
        {field: 'goodsName', title: '商品名称', align:'center',event: 'setGoods', style:'cursor: pointer;',width:200},
        {field: 'salePrice', title: '售价', align:'center',width:60},
        {field: 'goodsCode',title: '商品编号',align:'center',width:165},
        {field: 'lackLevel', title: '缺货紧急度', align:'center',templet: '#tpllackLevel',width:110},
        {field: 'stock', title: '库存', align:'center',sort: true,width:80},     
        {field: 'goodsStorageOutName',title: '所属出库批次',align:'center',event: 'setGoodsStorageOut',width:320,templet: '#tplGoodsStorageOut'},
        {field: 'goodsStorageInName',title: '所属入库批次',align:'center',width:320,templet: '#tplGoodsStorageIn'},
        {field: 'deviceIdJp',title: '设备编号(鲸品)',align:'center',width:125}, 
        {field: 'deviceIdSupp', title: '设备编号(供应商)', align:'center',width:140},
        {field: 'capacity', title: '最大容量', align:'center',sort: true,width:100},
        {field: 'warningNum', title: '报警临界值', align:'center',sort: true,width:110},
        {field: 'deviceId',minWidth:0,width:0,type:'space',style:'display:none'},
        {field: 'adsMiddleType',minWidth:0,width:0,type:'space',style:'display:none'},
        {field: 'right', title: '操作',align:'center', toolbar: "#rightToolBar"}
      ]],
      page: true,
      height: tableHeight
    });

    var $ = layui.$, active = {
      select: function () {
        var deviceId = $('#sltDeviceList').val();
        var goodsSkuId = $('#sltGoodsList').val();
        table.reload('roadList', {
          where: {
        	  deviceId: deviceId,
        	  goodsSkuId: goodsSkuId
          }
        });
      },
      reload:function(){       
       table.reload('roadList', {
          where: {
        	  deviceId: null,
        	  goodsSkuId: null
          }
        });
       $("#select2-sltDeviceList-container").text($("#sltDeviceList").find("option[value = '']").text());
       $("#sltDeviceList").val('');
       $("#select2-sltGoodsList-container").text($("#sltGoodsList").find("option[value = '']").text());
       $("#sltGoodsList").val('');
      },
      add: function () {
        add('添加货道', 'showAddRoad', 800, 500);
      },
      init: function () {
    	  init('初始化货道', 'showInitRoad', 750, 400);
        },
      update: function () {
        var checkStatus = table.checkStatus('roadList'), data = checkStatus.data;
        if (data.length != 1) {
          layer.msg('请选择一行编辑,已选['+data.length+']行', {icon: 5,time:1000});
          return false;
        }
        update('编辑货道', 'showUpdateRoad?id='+data[0].id, 900, 500);
      },
      excel: function () {
          var deviceId=$("#sltDeviceList").val();
          if(deviceId==null||deviceId==''){
              layer.msg('请选择一个设备', {icon: 5,time:1000});
              return false;
          }
      	window.location.href="roadExcel?deviceId="+deviceId;
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
      if(obj.event === 'setGoods'){
    	  setGoods('设置商品', 'showUpdateGoods?id='+data.id, 500, 300);
      }
      if(obj.event === 'setGoodsStorageOut'){
    	  setGoods('设置所属出库批次', 'showUpdateGoodsStorageOut?id='+data.id, 500, 200);
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
  
  function setGoods(title, url, w, h) {
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
	      id: 'road-setGoods',
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
  
  function init(title, url, w, h) {
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
	      id: 'road-init',
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
