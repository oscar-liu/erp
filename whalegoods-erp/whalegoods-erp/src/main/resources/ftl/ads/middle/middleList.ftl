<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>促销商品列表</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <!-- <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/> -->
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
    <button class="select-on layui-btn layui-btn-sm layui-btn-primary" data-type="select"><i class="layui-icon">&#xe615;</i>查询</button>
    <@shiro.hasPermission name="ads:middle:add"><button class="layui-btn layui-btn-normal layui-btn-sm" data-type="add"><i class="layui-icon">&#xe608;</i>新增</button></@shiro.hasPermission>
    <@shiro.hasPermission name="ads:middle:update"><button class="layui-btn  layui-btn-sm" data-type="update"><i class="layui-icon">&#xe642;</i>编辑</button></@shiro.hasPermission>
    &nbsp;&nbsp;&nbsp;<span style="color:red;">注意</span>：商品必须在'货道管理'中上架才能使促销活动生效
    <button class="layui-btn layui-btn-sm icon-position-button" id="refresh" style="float: right;" data-type="reload"><i class="layui-icon">&#x1002;</i></button>
  </div>
</div>
<table id="adsMiddleList" class="layui-hide" lay-filter="adsMiddle"></table>
<script type="text/html" id="rightToolBar">
<@shiro.hasPermission name="ads:middle:del"><a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a></@shiro.hasPermission>
</script>
<script type="text/html" id="tplType">
  {{#  if(d.type==1){ }}
    <span style="color:green;">整点</span>
  {{#  } else { }}
<span style="color:blue;">时间段</span>
  {{#  } }}
</script>
<script type="text/html" id="tplStock">
  {{#  if(d.stock ==null||d.stock ==undefined){ }}
    <span style="color:red;">未上架</span>
  {{#  } else { }}
<span>{{ d.stock}}</span>
  {{#  } }}
</script>
<script type="text/html" id="tplSalePrice">
  {{#  if(d.salePrice ==null||d.salePrice ==undefined ||d.salePrice ==''){ }}
    <span style="color:red;">未上架</span>
  {{#  } else { }}
<span>{{ d.salePrice}}</span>
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
      id: 'adsMiddleList',
      elem: '#adsMiddleList', 
      url: 'showAdsMiddleList',
      cols: [[
        {checkbox: true, fixed: true},
        {field: 'shortName', title: '点位短名', align:'center'},
        {field: 'goodsName', title: '促销商品', align:'center'},
        {field: 'stock', title: '库存', align:'center',templet: '#tplStock'},
        {field: 'salePrice', title: '售价', align:'center',templet: '#tplSalePrice'},
        {field: 'marketPrice',title: '原价',align:'center'},
        {field: 'timeRange', title: '时间范围', align:'center',sort: true},
        {field: 'type', title: '时间类型', align:'center',templet: '#tplType'},
        {field: 'right', title: '操作',align:'center', toolbar: "#rightToolBar"}
      ]],
      page: true,
      height: 'full-100'
    });

    var $ = layui.$, active = {
      select: function () {
        var deviceId = $('#sltDeviceList').val();
        table.reload('adsMiddleList', {
          where: {
        	  deviceId: deviceId
          }
        });
      },
      reload:function(){       
       table.reload('adsMiddleList', {
          where: {
        	  deviceId: null
          }
        });
       $("#select2-sltDeviceList-container").text($("#sltDeviceList").find("option[value = '']").text());
       $("#sltDeviceList").val('');
      },
      add: function () {
        add('添加促销商品（同一设备同一时间段最多只能有三个促销商品）', 'showAddAdsMiddle', 650, 400);
      },
      update: function () {
        var checkStatus = table.checkStatus('adsMiddleList'), data = checkStatus.data;
        if (data.length != 1) {
          layer.msg('请选择一行编辑,已选['+data.length+']行', {icon: 5,time:1000});
          return false;
        }
        update('编辑促销商品', 'showUpdateAdsMiddle?id=' + data[0].id, 650, 350);
      }
    };
    //监听工具条
    table.on('tool(adsMiddle)', function (obj) {
      var data = obj.data;
      if (obj.event === 'del') {
        layer.confirm('确定删除该促销商品?',{ title:'提示'},
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
      url:"delAdsMiddle",
      type:"post",
      data:{id:id},
      async:false,
      success:function(d){
        if(d.result_code==0){
          window.top.layer.msg(d.result_msg,{icon:6,time:1000});
          layui.table.reload('adsMiddleList');
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
      id: 'adsMiddle-update',
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
      id: 'adsMiddle-add',
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
