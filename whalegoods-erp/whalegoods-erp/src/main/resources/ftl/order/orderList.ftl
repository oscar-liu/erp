<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>订单列表</title>
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
   </div>&nbsp;
           日期范围：  <div class="layui-inline">
              <div class="layui-input-inline"><input type="text" class="layui-input" id="iptTimeRange" placeholder="开始 到 结束" style="width:177px;"></div>
  </div>&nbsp;&nbsp;
    <button class="select-on layui-btn layui-btn-sm layui-btn-primary" data-type="select"><i class="layui-icon">&#xe615;</i>查询</button>
    <@shiro.hasPermission name="order:sd:show"><button class="layui-btn layui-btn-normal layui-btn-sm" data-type="sdQuery"><i class="layui-icon">&#xe615;</i>刷单查询</button></@shiro.hasPermission>
    <@shiro.hasPermission name="order:sd:confirm"><button class="layui-btn layui-btn-normal layui-btn-sm" data-type="sdConfirm"><i class="layui-icon">&#x1005;</i>刷单确认</button></@shiro.hasPermission>
    <@shiro.hasPermission name="order:sd:excel"><button class="layui-btn layui-btn-normal layui-btn-sm" data-type="sdExcel"><i class="layui-icon">&#xe601;</i>导出刷单记录</button></@shiro.hasPermission>
    <button class="layui-btn layui-btn-sm icon-position-button" id="refresh" style="float: right;" data-type="reload"><i class="layui-icon">&#x1002;</i></button>
  </div>
</div>
<table id="orderList" class="layui-hide" lay-filter="order"></table>
<script type="text/html" id="tplPayType">
  {{#  if(d.payType==1){ }}
    <span style="color:green;">微信</span>
  {{#  } else { }}
<span style="color:blue;">支付宝</span>
  {{#  } }}
</script>

<script type="text/html" id="tplOrderStatus">
  {{#  if(d.orderStatus==1){ }}
    <span style="color:#FF6EB4;">未支付</span>
  {{#  } if (d.orderStatus==2) { }}
<span style="color:green;">已支付</span>
{{#  } if (d.orderStatus==3) { }}
<span style="color:red;">交易失败</span>
{{#  } if (d.orderStatus==4) { }}
<span style="color:#FFC0CB;">已退款</span>
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
  
  layui.use(['table','layer','laydate'], function () {
    var table = layui.table,layer = layui.layer,laydate = layui.laydate;
    laydate.render({
        elem: '#iptTimeRange'
        ,range: true
      });
    table.render({
      id: 'orderList',
      elem: '#orderList', 
      url: 'showOrderList',
      cols: [[
    	{checkbox: true, fixed: true},
    	{field: 'shortName',title: '点位短名',align:'center' }, 
        {field: 'orderTime',title: '订单时间',align:'center' },
        {field: 'goodsName', title: '商品名称', align:'center'},
        {field: 'orderStatus', title: '订单状态', align:'center',templet:'#tplOrderStatus'},
        {field: 'salePrice',title: '价格',align:'center', unresize: true},
        {field: 'payType', title: '支付方式', align:'center',templet: '#tplPayType'},        
        {field: 'orderId', title: '订单号', align:'center'},
        {field: 'orderType',minWidth:0,width:0,type:'space',style:'display:none'}
      ]],
      page: true,
      height: 'full-83'
    });

    var $ = layui.$, active = {
      select: function () {
    	var deviceId = $('#sltDeviceList').val();
    	var timeRange = $('#iptTimeRange').val();
        table.reload('orderList', {
          where: {
        	  deviceId: deviceId,
        	  timeRange: timeRange
          }
        });
      },
      sdQuery: function () {
      	var deviceId = $('#sltDeviceList').val();
      	var timeRange = $('#iptTimeRange').val();
          table.reload('orderList', {
            where: {
          	  deviceId: deviceId,
          	  timeRange: timeRange,
          	  orderType:2
            }
          });
        },
        sdConfirm: function () {
            var checkStatus = table.checkStatus('orderList'), data = checkStatus.data;
            if (data.length != 1) {
              layer.msg('请选择一行确认,已选['+data.length+']行', {icon: 5,time:1000});
              return false;
            }
            if (data[0].orderType != 2) {
                layer.msg('这不是一条刷单记录', {icon: 5,time:1000});
                return false;
              }
            sdConfirm(data[0].orderId);
          },
          sdExcel: function () {
            	var deviceId = $('#sltDeviceList').val();
              	var timeRange = $('#iptTimeRange').val();
              	window.location.href="sdExcel?deviceId="+deviceId+"&timeRange="+timeRange;
            },
      reload:function(){
        table.reload('orderList', {
          where: {
        	  deviceId: null,
        	  timeRange: null
          }
        });
        $("#iptTimeRange").attr("placeholder","开始 到 结束");
        $("#iptTimeRange").val('');
        $("#select2-sltDeviceList-container").text($("#sltDeviceList").find("option[value = '']").text());
        $("#sltDeviceList").val('');
      }
    };

    $('.select .layui-btn').on('click', function () {
      var type = $(this).data('type');
      active[type] ? active[type].call(this) : '';
    });

  });
  
  function sdConfirm(orderId) {
	    $.ajax({
	      url:"sdConfirm?orderId="+orderId,
	      dataType: 'json',
	      type:'get',
	      async:false,
	      success:function(d){
	        if(d.result_code==0){
	          window.top.layer.msg(d.result_msg,{icon:6,time:1000});
	          layui.table.reload('orderList');
	        }else{
	          window.top.layer.msg(d.result_msg,{icon:5,time:1000});
	        }},
	        error:function(){ 
	          window.top.layer.msg("确认失败,请联系管理员",{icon:5,time:1000});
	      }
	    });
	  }
</script>
</body>

</html>
