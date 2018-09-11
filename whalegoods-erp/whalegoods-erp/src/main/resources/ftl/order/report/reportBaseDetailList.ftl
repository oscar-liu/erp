<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>销售统计>明细</title>
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
   商品：
        <div class="layui-inline">
      <div class="layui-input-inline">
       <select id="sltGoodsCode" name="sltGoodsCode" lay-ignore>
     <option value="">直接选择或搜索选择</option>
  	<#list goodsList as goods>
          <option value="${goods.goodsCode}">${goods.goodsName}</option>
    </#list>
    </select>
      </div>
    </div>
           日期范围：  <div class="layui-inline">
              <div class="layui-input-inline"><input type="text" class="layui-input" id="iptDayRange" placeholder="开始 到 结束" style="width:177px;"></div>
  </div>&nbsp;&nbsp;
    <button class="select-on layui-btn layui-btn-sm layui-btn-primary" data-type="select"><i class="layui-icon">&#xe615;</i>查询</button>
    <@shiro.hasPermission name="order:reportDetail:excel"><button class="layui-btn  layui-btn-sm" data-type="excel"><i class="layui-icon">&#xe601;</i>导出明细EXCEL</button></@shiro.hasPermission>
    <@shiro.hasPermission name="order:reportDetail:excelReport"><button class="layui-btn  layui-btn-sm" data-type="excelReport"><i class="layui-icon">&#xe601;</i>导出统计报表EXCEL</button></@shiro.hasPermission>
    <button class="layui-btn layui-btn-sm icon-position-button" id="refresh" style="float: right;" data-type="reload"><i class="layui-icon">&#x1002;</i></button>
   </div>
</div>
<div class="layui-col-md12" style="height:26px;margin-top:2px;vertical-align:middle;text-align:center">
<span style="font-size:16px">总销量：<span style="color:red;" id="spnSalesCount">${total.salesCount?c}</span>&nbsp;&nbsp;总销售额：<span style="color:red;"  id="spnSalesAmount">${total.salesAmount?c}</span></span>
</div>
<table id="reportBaseDetailList" class="layui-hide" lay-filter="reportBaseDetail"></table>
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
	  $('#sltGoodsCode').select2();
  });
  
  layui.use(['table','layer','laydate'], function () {
    var table = layui.table,layer = layui.layer,laydate = layui.laydate,$ = layui.$;
    laydate.render({
        elem: '#iptDayRange'
        ,range: true
      });
    table.render({
      id: 'reportBaseDetailList',
      elem: '#reportBaseDetailList', 
      url: 'showReportBaseDetailList',
      cols: [[
        {field: 'orderDay',title: '订单日期',align:'center' },
    	{field: 'shortName',title: '点位短名',align:'center' }, 
        {field: 'goodsName',title: '商品名称',align:'center' },
        {field: 'salesCount', title: '销量', align:'center',sort: true},
        {field: 'salesAmount', title: '销售额', align:'center',sort: true}
        {field: 'costsAmount', title: '成本', align:'center',sort: true}
        {field: 'profit', title: '毛利', align:'center',sort: true}
        {field: 'costProfit', title: '成本利润率', align:'center',sort: true}
        {field: 'salesProfit', title: '销售利润率', align:'center',sort: true}
      ]],
      done:function(res, curr, count){
    	  var obj=new Object();
    	  obj.deviceId=$('#sltDeviceList').val();
    	  obj.goodsCode=$('#sltGoodsCode').val();
    	  obj.dayRange=$('#iptDayRange').val();
          $.ajax({
              url:'getTotalCountAndAmount',
              type:'get',
              data:obj,
              async:false,
              traditional: true,
              success:function(d){
            	  if(d==""||d==null||d==undefined){
                	  $("#spnSalesCount").text(0);
                	  $("#spnSalesAmount").text(0);
            	  }
            	  else{
                	  $("#spnSalesCount").text(d.salesCount);
                	  $("#spnSalesAmount").text(d.salesAmount);
            	  }
                },
                error:function(){
                  window.top.layer.msg('查询总销量和总销售额异常',{icon:5,time:1000});
            	  $("#spnSalesCount").text("查询异常");
            	  $("#spnSalesAmount").text("查询异常");
              }
            });
      },
      page: true,
      height: 'full-43'
    });

    var active = {
      select: function () {
    	var deviceId = $('#sltDeviceList').val();
    	var goodsCode = $('#sltGoodsCode').val();
    	var dayRange = $('#iptDayRange').val();
        table.reload('reportBaseDetailList', {
          where: {
        	  deviceId: deviceId,
        	  goodsCode: goodsCode,
        	  dayRange: dayRange
          }
        });
      },
      reload:function(){
        table.reload('reportBaseDetailList', {
          where: {
        	  deviceId: null,
        	  goodsCode: null,
        	  dayRange: null,
          }
        });
        $("#iptDayRange").attr("placeholder","开始 到 结束");
        $("#iptDayRange").val('');
        $("#select2-sltDeviceList-container").text($("#sltDeviceList").find("option[value = '']").text());
        $("#sltDeviceList").val('');
        $("#select2-sltGoodsCode-container").text($("#sltGoodsCode").find("option[value = '']").text());
        $("#sltGoodsCode").val('');
      },
      excel: function () {
      	var deviceId = $('#sltDeviceList').val();
    	var goodsCode = $('#sltGoodsCode').val();
    	var dayRange = $('#iptDayRange').val();
          if(dayRange==null||dayRange==''){
              layer.msg('请选择一个日期范围', {icon: 5,time:1000});
              return false;
          }
      	window.location.href="reportDetailExcel?deviceId="+deviceId+"&goodsCode="+goodsCode+"&dayRange="+dayRange;
        },
        excelReport: function () {
          	var deviceId = $('#sltDeviceList').val();
        	var goodsCode = $('#sltGoodsCode').val();
        	var dayRange = $('#iptDayRange').val();
              if(dayRange==null||dayRange==''){
                  layer.msg('请选择一个日期范围', {icon: 5,time:1000});
                  return false;
              }
          	window.location.href="reportReportExcel?deviceId="+deviceId+"&goodsCode="+goodsCode+"&dayRange="+dayRange;
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
