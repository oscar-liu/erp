<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>销售统计>按设备</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
  <link rel="stylesheet" href="${re.contextPath}/plugin/layui/css/layui.css">
  <link rel="stylesheet" href="${re.contextPath}/plugin/erp/main.css">
  <link rel="stylesheet" href="${re.contextPath}/plugin/select2/css/select2.css">
  <script type="text/javascript" src="${re.contextPath}/plugin/jquery/jquery-3.2.1.min.js"></script>
  <script type="text/javascript" src="${re.contextPath}/plugin/select2/js/select2.min.js"></script>
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
           日期范围：  <div class="layui-inline">
              <div class="layui-input-inline"><input type="text" class="layui-input" id="iptDayRange" placeholder="开始 到 结束" style="width:177px;"></div>
  </div>&nbsp;&nbsp;
    <button class="select-on layui-btn layui-btn-sm layui-btn-primary" data-type="select"><i class="layui-icon">&#xe615;</i>查询</button>&nbsp;&nbsp;<span>总销量：</span>
    <button class="layui-btn layui-btn-sm icon-position-button" id="refresh" style="float: right;" data-type="reload"><i class="layui-icon">&#x1002;</i></button>
   </div>
</div>
<table id="reportByDeviceList" class="layui-hide" lay-filter="reportByDevice"></table>
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
  
  layui.use(['table','layer','laydate'], function () {
    var table = layui.table,layer = layui.layer,laydate = layui.laydate;
    laydate.render({
        elem: '#iptDayRange'
        ,range: true
      });
    table.render({
      id: 'reportByDeviceList',
      elem: '#reportByDeviceList', 
      url: 'showReportByDeviceList',
      cols: [[
    	{field: 'shortName',title: '点位短名',align:'center' }, 
        {field: 'orderDay',title: '订单日期',align:'center' },
        {field: 'salesCount', title: '销量', align:'center'},
        {field: 'salesAmount', title: '销售额', align:'center'}
      ]],
      page: true,
      height: 'full-83'
    });

    var $ = layui.$, active = {
      select: function () {
    	var deviceId = $('#sltDeviceList').val();
    	var dayRange = $('#iptDayRange').val();
        table.reload('reportByDeviceList', {
          where: {
        	  deviceId: deviceId,
        	  dayRange: dayRange
          }
        });
      },
      reload:function(){
        table.reload('reportByDeviceList', {
          where: {
        	  deviceId: null,
        	  dayRange: null,
          }
        });
        $("#iptDayRange").attr("placeholder","开始 到 结束");
        $("#iptDayRange").val('');
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
