<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>内部购买登记列表</title>
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
   </div>&nbsp;
     购买日期：  <div class="layui-inline">
              <div class="layui-input-inline"><input type="text" class="layui-input" id="iptTimeRange" placeholder="开始 到 结束" style="width:177px;"></div>
  </div>
    <button class="select-on layui-btn layui-btn-sm layui-btn-primary" data-type="select"><i class="layui-icon">&#xe615;</i>查询</button>
    <@shiro.hasPermission name="storage:ir:add"><button class="layui-btn layui-btn-normal layui-btn-sm" data-type="add"><i class="layui-icon">&#xe608;</i>登记</button></@shiro.hasPermission>
    <button class="layui-btn layui-btn-sm icon-position-button" id="refresh" style="float: right;" data-type="reload"><i class="layui-icon">&#x1002;</i></button>
  </div>
</div>
<table id="storageIrList" class="layui-hide" lay-filter="storageIr"></table>
<script type="text/html" id="rightToolBar">
<@shiro.hasPermission name="storage:ir:del"><a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a></@shiro.hasPermission>
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
	  $('#sltGoodsList').select2();
  });
  
  layui.use('table', function () {
    var table = layui.table,laydate = layui.laydate;
    laydate.render({
        elem: '#iptTimeRange'
        ,range: true
      });
    table.render({
      id: 'storageIrList',
      elem: '#storageIrList',
      url: 'showGoodsStorageIrList',
      cols: [[
        {field: 'saleDate', title: '购买日期', align:'center',width:110},
        {field: 'goodsName', title: '商品名称', align:'center',width:220},
        {field: 'goodsCode',title: '商品编号',align:'center',width:150},
        {field: 'goodsStorageInName',title: '所属入库批次',align:'center',width:320},
        {field: 'saleCount', title: '数量（个）', align:'center',width:130},
        {field: 'salePrice',title: '价格',align:'center',width:70},
        {field: 'totalSalePrice',title: '总金额',align:'center',width:70},
        {field: 'remark', title: '备注', align:'center',width:200},
        {field: 'right', title: '操作',align:'center', toolbar: "#rightToolBar",width:70}
      ]],
      page: true,
      height:  'full-80'
    });

    var $ = layui.$, active = {
      select: function () {
        var goodsSkuId = $('#sltGoodsList').val();
        var timeRange = $('#iptTimeRange').val();
        table.reload('storageIrList', {
          where: {
        	  goodsSkuId: goodsSkuId,
        	  timeRange:timeRange
          }
        });
      },
      reload:function(){       
       table.reload('storageIrList', {
          where: {
        	  goodsSkuId: null,
        	  timeRange:null
          }
        });
       $("#iptTimeRange").attr("placeholder","开始 到 结束");
       $("#iptTimeRange").val('');
       $("#select2-sltGoodsList-container").text($("#sltGoodsList").find("option[value = '']").text());
       $("#sltGoodsList").val('');
      },
      add: function () {
    	  add('内部购买登记', 'showAddGoodsStorageIr',700,560);
      }
    };
    
    //监听工具条
    table.on('tool(storageIr)', function (obj) {
      var data = obj.data;
      if (obj.event === 'del') {
        layer.confirm('确定删除该内部购买记录?',{ title:'提示'},
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
      url:"delGoodsStorageIr",
      type:"post",
      data:{id:id},
      async:false,
      success:function(d){
        if(d.result_code==0){
          window.top.layer.msg(d.result_msg,{icon:6,time:1000});
          layui.table.reload('storageIrList');
        }else{
          window.top.layer.msg(d.result_msg);
        }},
        error:function(){ 
          window.top.layer.msg("删除失败,请联系管理员",{icon:5,time:1000});
      }
    });
  }
  
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
      id: 'storageIr-add',
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
