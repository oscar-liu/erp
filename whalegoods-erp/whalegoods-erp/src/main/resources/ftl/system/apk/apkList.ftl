<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>APK列表</title>
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
           版本号： <div class="layui-inline"><input class="layui-input" height="20px" id="apkVersion" autocomplete="off"></div>
    <button class="select-on layui-btn layui-btn-sm layui-btn-primary" data-type="select"><i class="layui-icon">&#xe615;</i>模糊查询</button>
    <@shiro.hasPermission name="apk:add"> <button class="layui-btn layui-btn-normal layui-btn-sm" data-type="add"><i class="layui-icon">&#xe608;</i>上传APK</button></@shiro.hasPermission>
    <@shiro.hasPermission name="apk:update"><button class="layui-btn  layui-btn-sm" data-type="update"><i class="layui-icon">&#xe642;</i>编辑</button></@shiro.hasPermission>
    <button class="layui-btn layui-btn-sm icon-position-button" id="refresh" style="float: right;" data-type="reload"><i class="layui-icon">&#x1002;</i></button>
  </div>
</div>
<table id="apkList" class="layui-hide" lay-filter="apk"></table>
<script type="text/html" id="rightToolBar">
<@shiro.hasPermission name="apk:del"><a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a></@shiro.hasPermission>
</script>
<script type="text/html" id="switchTpl">
  <input type="checkbox" name="apkStatus" value="{{d.id}}" lay-skin="switch" lay-text="启用|停用" lay-filter="radioApkStatus" {{ d.apkStatus == 1 ? 'checked' : '' }}>
</script>
<script type="text/html" id="tplApk">
  {{#  if(d.apkUrl==null||d.apkUrl==undefined||d.apkUrl==""){ }}
    <span style="color: #F581B1;">无</span>
  {{#  } else { }}
<a href="{{d.apkUrl}}" class="layui-table-link" target="_blank">下载</a>
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
  
  layui.use('table', function () {
    var table = layui.table,form = layui.form;
    table.render({
      id: 'apkList',
      elem: '#apkList', 
      url: 'showApkList',
      cols: [[
        {checkbox: true, fixed: true},
        {field: 'apkVersion',title: '版本号',align:'center' }, 
        {field: 'apkUrl', title: 'APK文件', align:'center', templet: '#tplApk'},
        {field: 'apkStatus',title: '状态',align:'center',templet: '#switchTpl', unresize: true},
        {field: 'right', title: '操作',align:'center', toolbar: "#rightToolBar"}
      ]],
      page: true,
      height: 'full-46'
    });
    
    form.on('switch(radioApkStatus)', function(obj){
        $.ajax({
            url:"updateApkStatus",
            type:"post",
            data:{id:this.value,apk_status:obj.elem.checked},
            async:false,
            success:function(r){
              if(r.result_code==0){
                window.top.layer.msg(r.result_msg,{icon:6,time:1000});
              }else{
                window.top.layer.msg(r.result_msg,{icon:5,time:1000});
              }
            },
            error:function(){
            	window.top.layer.msg("设置失败,请联系管理员",{icon:5,time:1000});
            }
          });
    });

    var $ = layui.$, active = {
      select: function () {
        var apkVersion = $('#apkVersion').val();
        table.reload('apkList', {
          where: {
        	  apkVersion: apkVersion,
          }
        });
      },
      reload:function(){
        $('#apkVersion').val('');
        table.reload('apkList', {
          where: {
        	  apkVersion: null,
          }
        });
      },
      add: function () {
        add('上传APK', 'showAddApk', 700, 450);
      },
      update: function () {
        var checkStatus = table.checkStatus('apkList'), data = checkStatus.data;
        if (data.length != 1) {
          layer.msg('请选择一行编辑,已选['+data.length+']行', {icon: 5,time:1000});
          return false;
        }
        update('编辑APK', 'updateApk?id=' + data[0].id, 700, 450);
      }
    };
    //监听工具条
    table.on('tool(apk)', function (obj) {
      var data = obj.data;
      if (obj.event === 'del') {
        layer.confirm('确定删除版本：[<label style="color: #00AA91;">' + data.apkVersion + '</label>]的APK包?',{ title:'提示'},
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
      url:"delApk",
      type:"post",
      data:{id:id},
      async:false,
      success:function(d){
        if(d.result_code==0){
          window.top.layer.msg(d.result_msg,{icon:6,time:1000});
          layui.table.reload('apkList');
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
      id: 'apk-update',
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
      id: 'apk-add',
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
