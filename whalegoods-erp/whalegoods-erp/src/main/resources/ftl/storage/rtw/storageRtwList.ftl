<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>商品返仓列表</title>
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
<input id="iptPhdDeviceId" type="hidden" name="iptPhdDeviceId">
<input id="iptPhdRtwDay" type="hidden" name="iptPhdRtwDay">
<input id="iptPhdRemark" type="hidden" name="iptPhdRemark">
  <div class="select">
   商品：
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
    <@shiro.hasPermission name="storage:rtw:add"><button class="layui-btn layui-btn-normal layui-btn-sm" data-type="add"><i class="layui-icon">&#xe608;</i>返仓</button></@shiro.hasPermission>
    <button class="layui-btn layui-btn-sm icon-position-button" id="refresh" style="float: right;" data-type="reload"><i class="layui-icon">&#x1002;</i></button>
  </div>
</div>
<table id="storageRtwList" class="layui-hide" lay-filter="storageRtw"></table>
<script type="text/html" id="rightToolBar">
<@shiro.hasPermission name="storage:rtw:del"><a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a></@shiro.hasPermission>
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
    var table = layui.table;
    table.render({
      id: 'storageRtwList',
      elem: '#storageRtwList', 
      url: 'showGoodsStorageRtwList',
      cols: [[
        {field: 'rtwDay', title: '返仓日期', align:'center',width:120},
        {field: 'shortName', title: '点位', align:'center',width:115},
        {field: 'goodsName', title: '商品名称', align:'center',width:220},
        {field: 'goodsCode',title: '商品编号',align:'center',width:150},
        {field: 'rtwNum', title: '数量（个）', align:'center',width:115,sort: true},
        {field: 'goodsStorageInName', title: '所属入库批次', align:'center',width:315},
        {field: 'remark', title: '备注', align:'center',width:200},
        {field: 'right', title: '操作',align:'center', toolbar: "#rightToolBar",width:70}
      ]],
      page: true,
      height:  'full-43'
    });

    var $ = layui.$, active = {
      select: function () {
        var goodsSkuId = $('#sltGoodsList').val();
        table.reload('storageRtwList', {
          where: {
        	  goodsSkuId: goodsSkuId
          }
        });
      },
      reload:function(){       
       table.reload('storageRtwList', {
          where: {
        	  goodsSkuId: null
          }
        });
       $("#select2-sltGoodsList-container").text($("#sltGoodsList").find("option[value = '']").text());
       $("#sltGoodsList").val('');
      },
      add: function () {
    	var deviceId=$("#iptPhdDeviceId").val();
    	var rtwDay=$("#iptPhdRtwDay").val();
    	var remark=$("#iptPhdRemark").val();
    	if((deviceId!=''&&deviceId!=null)&&rtwDay!=null){
    		add('商品返仓', 'showAddGoodsStorageRtw?deviceId='+deviceId+'&rtwDay='+rtwDay+'&remark='+remark, 700,500);	
    	}
    	else{
    		add('商品返仓', 'showAddGoodsStorageRtw', 700,500);
    	}
      }
    };
    
    //监听工具条
    table.on('tool(storageRtw)', function (obj) {
      var data = obj.data;
      if (obj.event === 'del') {
        layer.confirm('确定删除该返仓记录?',{ title:'提示'},
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
      url:"delGoodsStorageRtw",
      type:"post",
      data:{id:id},
      async:false,
      success:function(d){
        if(d.result_code==0){
          window.top.layer.msg(d.result_msg,{icon:6,time:1000});
          layui.table.reload('storageRtwList');
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
      id: 'storageRtw-add',
      type: 2,
      area: [w + 'px', h + 'px'],
      fix: false,
      maxmin: true,
      shadeClose: false,
      shade: 0.4,
      title: title,
      content: url,
      yes : function(index , layero){
          layer.close(index);
      }
    });
  }
</script>
</body>

</html>
