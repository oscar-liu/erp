<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>库位信息列表</title>
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
           库位名称： <div class="layui-inline"><input class="layui-input" height="20px" id="iptLocationName" autocomplete="off"></div>
    <button class="select-on layui-btn layui-btn-sm layui-btn-primary" data-type="select"><i class="layui-icon">&#xe615;</i>查询</button>
    <@shiro.hasPermission name="goods:storage:location:add"> <button class="layui-btn layui-btn-normal layui-btn-sm" data-type="add"><i class="layui-icon">&#xe608;</i>新增</button></@shiro.hasPermission>
    <@shiro.hasPermission name="goods:storage:location:update"><button class="layui-btn  layui-btn-sm" data-type="update"><i class="layui-icon">&#xe642;</i>编辑</button></@shiro.hasPermission>
    <button class="layui-btn layui-btn-sm icon-position-button" id="refresh" style="float: right;" data-type="reload"><i class="layui-icon">&#x1002;</i></button>
  </div>
</div>
<table id="goodsStorageLocationList" class="layui-hide" lay-filter="goodsStorageLocation"></table>
<script type="text/html" id="rightToolBar">
<@shiro.hasPermission name="goods:storage:location:update"><a class="layui-btn layui-btn-xs  layui-btn-normal" lay-event="edit">编辑</a></@shiro.hasPermission>
<@shiro.hasPermission name="goods:storage:location:del"><a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a></@shiro.hasPermission>
</script>
<script>
  document.onkeydown = function (e) {
    var theEvent = window.event || e;
    var code = theEvent.keyCode || theEvent.which;
    if (code == 13) {
      $(".select .select-on").click();
    }
  }
  layui.use('table', function () {
    var table = layui.table,form = layui.form;
    table.render({
      id: 'goodsStorageLocationList',
      elem: '#goodsStorageLocationList', 
      url: 'showGoodsStorageLocationList',
      cols: [[
        {checkbox: true, fixed: true},
        {field: 'locationName',title: '库位名称',align:'center',width:165}, 
        {field: 'createName',title: '创建人',align:'center',width:120},
        {field: 'createDate',title: '创建日期',align:'center',width:160},
        {field: 'updateName',title: '修改人',align:'center',width:120},
        {field: 'updateDate',title: '修改日期',align:'center',width:160},
        {field: 'right', title: '操作',align:'center', toolbar: "#rightToolBar",width:125}
      ]],
      page: true,
      height: 'full-46'
    });

    var $ = layui.$, active = {
      select: function () {
        var locationName = $('#iptLocationName').val();
        table.reload('goodsStorageLocationList', {
          where: {
        	  locationName: locationName
          }
        });
      },
      reload:function(){
        $('#iptLocationName').val('');
        table.reload('goodsStorageLocationList', {
          where: {
        	  locationName: null
          }
        });
      },
      add: function () {
        add('添加库位', 'showAddStorageLocation',350,200);
      },
      update: function () {
        var checkStatus = table.checkStatus('goodsStorageLocationList'), data = checkStatus.data;
        if (data.length != 1) {
          layer.msg('请选择一行编辑,已选['+data.length+']行', {icon: 5,time:1000});
          return false;
        }
        update('编辑库位', 'showUpdateStorageLocation?id=' + data[0].id,350,200);
      }
    };
    //监听工具条
    table.on('tool(goodsStorageLocation)', function (obj) {
      var data = obj.data;
      if (obj.event === 'del') {
        layer.confirm('确定删除库位[<label style="color: #00AA91;">' + data.locationName + '</label>]?',{ title:'提示'},
        function (index) {
          del(data.id);
          layer.close(index);
        });
      } 
      else if (obj.event === 'edit') {
        update('编辑库位', 'showUpdateStorageLocation?id=' + data.id,350,200);
      }
    });

    $('.select .layui-btn').on('click', function () {
      var type = $(this).data('type');
      active[type] ? active[type].call(this) : '';
    });

  });

  function del(id) {
    $.ajax({
      url:"delStorageLocation",
      type:"post",
      data:{id:id},
      async:false,
      success:function(d){
        if(d.result_code==0){
          window.top.layer.msg(d.result_msg,{icon:6,time:1000});
          layui.table.reload('goodsStorageLocationList');
        }else{
          window.top.layer.msg(d.result_msg);
        }},
        error:function(){
          window.top.layer.msg("删除失败,请联系管理员",{icon:5});
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
      id: 'goodsStorageLocation-update',
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
      id: 'goodsStorageLocation-add',
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
