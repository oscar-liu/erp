<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>型号标准列表</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=standard-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
  <link rel="stylesheet" href="${re.contextPath}/plugin/layui/css/layui.css">
  <link rel="stylesheet" href="${re.contextPath}/plugin/erp/main.css">
  <script type="text/javascript" src="${re.contextPath}/plugin/jquery/jquery-3.2.1.min.js"></script>
  <script type="text/javascript" src="${re.contextPath}/plugin/layui/layui.all.js" charset="utf-8"></script>
</head>

<body>
<div class="erp-search">
  <div class="select">
    <@shiro.hasPermission name="standard:set"><button class="layui-btn  layui-btn-sm" data-type="setStandard"><i class="layui-icon">&#xe642;</i>设置型号标准</button></@shiro.hasPermission>
    <button class="layui-btn layui-btn-sm icon-position-button" id="refresh" style="float: right;" data-type="reload"><i class="layui-icon">&#x1002;</i></button>
  </div>
</div>
<table id="deviceModelStandardList" class="layui-hide" lay-filter="standard"></table>
<script>
  
  layui.use('table', function () {
    var table = layui.table,form = layui.form;
    table.render({
      id: 'deviceModelStandardList',
      elem: '#deviceModelStandardList', 
      url: 'showDeviceModelStandardList',
      cols: [[
        {field: 'modelName',title: '型号名称',align:'center',sort: true}, 
        {field: 'ctn', title: '柜号', align:'center',sort: true},
        {field: 'floor',title: '层号',align:'center',sort: true},
        {field: 'pathNum', title: '最大货道数', align:'center',sort: true}
      ]],
      page: true,
      height: 'full-83'
    });

    var $ = layui.$, active = {
      reload:function(){
        table.reload('deviceModelStandardList', {
          where: {
          }
        });
      },
      setStandard: function () {
    	  setStandard('设置型号标准', 'showSetDeviceModelStandard', 600, 800);
      }
    };

    $('.select .layui-btn').on('click', function () {
      var type = $(this).data('type');
      active[type] ? active[type].call(this) : '';
    });

  });

  function setStandard(title, url, w, h) {
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
      id: 'standard-add',
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
