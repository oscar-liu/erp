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
  <script type="text/javascript" src="${re.contextPath}/plugin/jquery/jquery-3.2.1.min.js"></script>
  <script type="text/javascript" src="${re.contextPath}/plugin/layui/layui.all.js" charset="utf-8"></script>
</head>

<body>
<div class="erp-search">
  <div class="select">
           点位短名： <div class="layui-inline"><input class="layui-input" height="20px" id="shortName" autocomplete="off"></div>
           设备编号（鲸品）： <div class="layui-inline"><input class="layui-input" height="20px" id="shortName" autocomplete="off"></div>
           开始日期： <div class="layui-inline"><input type="text"  id="startOrderTime" name="startOrderTime" placeholder="开始时间"  autocomplete="off" class="layui-input"></div>
           结束日期： <div class="layui-inline"><input type="text"  id="endOrderTime" name="endOrderTime" placeholder="结束时间"  autocomplete="off" class="layui-input"></div>
    <button class="select-on layui-btn layui-btn-sm layui-btn-primary" data-type="select"><i class="layui-icon">&#xe615;</i>查询</button>
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
  {{#  } else if (d.orderStatus==2) { }}
<span style="color:green;">已支付</span>
  {{#  } }
{{#  } else if (d.orderStatus==3) { }}
<span style="color:#FFC0CB;">交易失败</span>
  {{#  } }
{{#  } else if (d.orderStatus==4) { }}
<span style="color:red;">已退款</span>
  {{#  } }
}
</script>
<script>

  document.onkeydown = function (e) {
    var theEvent = window.event || e;
    var code = theEvent.keyCode || theEvent.which;
    if (code == 13) {
      $(".select .select-on").click();
    }
  }
  
  layui.use(['table','layer','laydate'], function () {
    var table = layui.table,layer = layui.layer,laydate = layui.laydate;
    laydate.render({
        elem: '#startOrderTime',
        max:0,
        done:function(value, date){
        	 laydate.render({
        	        elem: '#endOrderTime',
        	        max:value.substring(0.8)+'31 '
        	      });
        }
      });
    laydate.render({
        elem: '#endOrderTime',
        max:0
      });
    table.render({
      id: 'orderList',
      elem: '#orderList', 
      url: 'showOrderList',
      cols: [[
        {field: 'orderTime',title: '订单时间',align:'center' },
        {field: 'goodsName', title: '商品名称', align:'center'},
        {field: 'orderStatus', title: '订单状态', align:'center',templet:'tplOrderStatus'},
        {field: 'salePrice',title: '价格',align:'center', unresize: true},
        {field: 'payType', title: '支付方式', align:'center',templet: '#tplPayType'},
        {field: 'shortName',title: '点位短名',align:'center' }, 
        {field: 'orderId', title: '订单号', align:'center'}
      ]],
      page: true,
      height: 'full-83'
    });

    var $ = layui.$, active = {
      select: function () {
        var shortName = $('#shortName').val();
        var startOrderTime = $('#startOrderTime').val();
        var endOrderTime = $('#endOrderTime').val();
        table.reload('orderList', {
          where: {
        	  shortName: shortName,
        	  startOrderTime: startOrderTime,
        	  endOrderTime: endOrderTime,
          }
        });
      },
      reload:function(){
        $('#shortName').val('');
        $('#startOrderTime').val('');
        $('#endOrderTime').val('');
        table.reload('orderList', {
          where: {
        	  shortName: null,
        	  startOrderTime: null,
        	  endOrderTime: null,
          }
        });
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
