<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>仓库库存信息列表</title>
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
    <button class="layui-btn layui-btn-sm icon-position-button" id="refresh" style="float: right;" data-type="reload"><i class="layui-icon">&#x1002;</i></button>
  </div>
</div>
<table id="storageList" class="layui-hide" lay-filter="storage"></table>
<script>
  document.onkeydown = function (e) {
    var theEvent = window.event || e;
    var code = theEvent.keyCode || theEvent.which;
    if (code == 13) {
      $(".select .select-on").click();
    }
  }
  
  $(function(){
	  $('#sltGoodsList').select2();
  });
  
  layui.use('table', function () {
    var table = layui.table;
    table.render({
      id: 'storageList',
      elem: '#storageList', 
      url: 'showGoodsStorageList',
      cols: [[
        {field: 'goodsName', title: '商品名称', align:'center',width:250},
        {field: 'goodsCode',title: '商品编号',align:'center',width:155},
        {field: 'expiringDate',title: '到期日期',align:'center',width:145},
        {field: 'currCount', title: '库存（个）', align:'center',width:130}
      ]],
      page: true,
      height:  'full-43'
    });

    var $ = layui.$, active = {
      select: function () {
        var goodsSkuId = $('#sltGoodsList').val();
        table.reload('storageList', {
          where: {
        	  goodsSkuId: goodsSkuId
          }
        });
      },
      reload:function(){       
       table.reload('storageList', {
          where: {
        	  goodsSkuId: null
          }
        });
       $("#select2-sltGoodsList-container").text($("#sltGoodsList").find("option[value = '']").text());
       $("#sltGoodsList").val('');
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
