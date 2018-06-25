<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>销售统计>按商品</title>
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
    <button class="layui-btn layui-btn-sm icon-position-button" id="refresh" style="float: right;" data-type="reload"><i class="layui-icon">&#x1002;</i></button>
   </div>
</div>
<table id="reportByGoodsList" class="layui-hide" lay-filter="reportByGoods"></table>
<script>
  document.onkeydown = function (e) {
    var theEvent = window.event || e;
    var code = theEvent.keyCode || theEvent.which;
    if (code == 13) {
      $(".select .select-on").click();
    }
  }
  
  $(function(){
	  $('#sltGoodsCode').select2();
  });
  
  layui.use(['table','layer','laydate'], function () {
    var table = layui.table,layer = layui.layer,laydate = layui.laydate;
    laydate.render({
        elem: '#iptDayRange'
        ,range: true
      });
    table.render({
      id: 'reportByGoodsList',
      elem: '#reportByGoodsList', 
      url: 'showReportByGoodsList',
      cols: [[
    	{field: 'orderDay',title: '订单日期',align:'center' },
    	{field: 'goodsName',title: '商品名称',align:'center' }, 
        {field: 'salesCount', title: '销量', align:'center',sort: true},
        {field: 'salesAmount', title: '销售额', align:'center',sort: true}
      ]],
      page: true,
      height: 'full-83'
    });

    var $ = layui.$, active = {
      select: function () {
    	var goodsCode = $('#sltGoodsCode').val();
    	var dayRange = $('#iptDayRange').val();
        table.reload('reportByGoodsList', {
          where: {
        	  goodsCode: goodsCode,
        	  dayRange: dayRange
          }
        });
      },
      reload:function(){
        table.reload('reportByGoodsList', {
          where: {
        	  goodsCode: null,
        	  dayRange: null,
          }
        });
        $("#iptDayRange").attr("placeholder","开始 到 结束");
        $("#iptDayRange").val('');
        $("#select2-sltGoodsCode-container").text($("#sltGoodsCode").find("option[value = '']").text());
        $("#sltGoodsCode").val('');
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
