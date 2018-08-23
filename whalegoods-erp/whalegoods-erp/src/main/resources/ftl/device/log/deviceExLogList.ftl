<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>设备异常信息列表</title>
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
    <button class="select-on layui-btn layui-btn-sm layui-btn-primary" data-type="select"><i class="layui-icon">&#xe615;</i>查询</button>
    <button class="layui-btn layui-btn-sm icon-position-button" id="refresh" style="float: right;" data-type="reload"><i class="layui-icon">&#x1002;</i></button>
  </div>
</div>
<table id="deviceExLogList" class="layui-hide" lay-filter="deviceExLog"></table>
<script type="text/html" id="tplFileUrl">
  {{#  if(d.fileUrl==null||d.fileUrl==undefined||d.fileUrl==""){ }}
    <span style="color: #F581B1;">无</span>
  {{#  } else { }}
<a href="{{d.fileUrl}}" class="layui-table-link" target="_blank">下载</a>
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
  });
  
  layui.use('table', function () {
    var table = layui.table;
    table.render({
      id: 'deviceExLogList',
      elem: '#deviceExLogList', 
      url: 'showDeviceExLogList',
      cols: [[
        {field: 'orderId', title: '订单号', align:'center',width:300},
        {field: 'shortName', title: '点位', align:'center',width:120},
        {field: 'goodsName', title: '商品名称', align:'center',width:200},
        {field: 'errorMessage', title: '异常信息', align:'center',width:220},
        {field: 'fileUrl', title: '详情文件', align:'center',width:100,templet:'#tplFileUrl'}
      ]],
      page: true,
      height: 'full-63'
    });

    var $ = layui.$, active = {
      select: function () {
        var deviceId = $('#sltDeviceList').val();
        table.reload('deviceExLogList', {
          where: {
        	  deviceId: deviceId
          }
        });
      },
      reload:function(){       
       table.reload('deviceExLogList', {
          where: {
        	  deviceId: null
          }
        });
       $("#select2-sltDeviceList-container").text($("#sltDeviceList").find("option[value = '']").text());
       $("#sltDeviceList").val('');
      }
    };

    $('.select .layui-btn').on('click', function () {
      var type = $(this).data('type');
      active[type] ? active[type].call(this) : '';
    });

  });
</script>
</body>

</html>
