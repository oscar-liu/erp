<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>系统用户管理</title>
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
           用户名： <div class="layui-inline"><input class="layui-input" height="20px" id="uname" autocomplete="off"></div>
    <button class="select-on layui-btn layui-btn-sm layui-btn-primary" data-type="select"><i class="layui-icon">&#xe615;</i>查询</button>
    <@shiro.hasPermission name="user:select"> <button class="layui-btn layui-btn-normal layui-btn-sm" data-type="add"><i class="layui-icon">&#xe608;</i>新增</button></@shiro.hasPermission>
    <@shiro.hasPermission name="user:update"><button class="layui-btn  layui-btn-sm" data-type="update"><i class="layui-icon">&#xe642;</i>编辑</button></@shiro.hasPermission>
    <@shiro.hasPermission name="user:repass"><button class="layui-btn layui-btn-warm layui-btn-sm" data-type="changePwd"><i class="layui-icon">&#xe614;</i>修改密码</button></@shiro.hasPermission>
    <button class="layui-btn layui-btn-sm icon-position-button" id="refresh" style="float: right;" data-type="reload"><i class="layui-icon">&#x1002;</i></button>
  </div>
</div>
<table id="userList" class="layui-hide" lay-filter="user"></table>
<script type="text/html" id="rightToolBar">
<@shiro.hasPermission name="user:update"><a class="layui-btn layui-btn-xs  layui-btn-normal" lay-event="edit">编辑</a></@shiro.hasPermission>
<@shiro.hasPermission name="user:del"><a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a></@shiro.hasPermission>
</script>
<script type="text/html" id="switchTpl">
  <input type="checkbox" name="accountStatus" value="{{d.id}}" lay-skin="switch" lay-text="开启|禁用" lay-filter="radioAccountStatus" {{ d.accountStatus == 1 ? 'checked' : '' }}>
</script>
<script type="text/html" id="tplheadPicUrl">
  {{#  if(d.headPicUrl==null||d.headPicUrl==undefined||d.headPicUrl==""){ }}
    <span style="color: #F581B1;">无</span>
  {{#  } else { }}
<a href="{{d.headPicUrl}}" class="layui-table-link" target="_blank">查看</a>
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
      id: 'userList',
      elem: '#userList', 
      url: 'showUserList',
      cols: [[
        {checkbox: true, fixed: true},
        {field: 'userName',title: '用户名',align:'center' }, 
        {field: 'headPicUrl', title: '头像', align:'center', templet: '#tplheadPicUrl'},
        {field: 'phone', title: '手机号', align:'center'},
        {field: 'email', title: '邮箱地址', align:'center'},
        {field: 'accountStatus',title: '账号状态',align:'center',templet: '#switchTpl', unresize: true},
        {field: 'right', title: '操作',align:'center', toolbar: "#rightToolBar"}
      ]],
      page: true,
      height: 'full-46'
    });
    
    form.on('switch(radioAccountStatus)', function(obj){
        $.ajax({
            url:"updateAccountStatus",
            type:"post",
            data:{id:this.value,account_status:obj.elem.checked},
            async:false,
            success:function(r){
              if(r.result_code==0){
                window.top.layer.msg(r.result_msg,{icon:6});
              }else{
                window.top.layer.msg(r.result_msg,{icon:5});
              }
            },
            error:function(){
            	window.top.layer.msg("设置失败,请联系管理员",{icon:5});
            }
          });
    });

    var $ = layui.$, active = {
      select: function () {
        var uname = $('#uname').val();
        table.reload('userList', {
          where: {
        	  userName: uname,
          }
        });
      },
      reload:function(){
        $('#uname').val('');
        table.reload('userList', {
          where: {
        	  userName: null,
          }
        });
      },
      add: function () {
        add('添加用户', 'showAddUser', 700, 600);
      },
      update: function () {
        var checkStatus = table.checkStatus('userList'), data = checkStatus.data;
        if (data.length != 1) {
          layer.msg('请选择一行编辑,已选['+data.length+']行', {icon: 5,time:1000});
          return false;
        }
        update('编辑用户', 'showUpdateUser?id=' + data[0].id, 700, 600);
      },
      changePwd:function(){
        var checkStatus = table.checkStatus('userList')
            , data = checkStatus.data;
        if (data.length != 1) {
          layer.msg('请选择一个用户,已选['+data.length+']行', {icon: 5,time:1000});
          return false;
        }
        rePwd('修改密码','goRePass?id='+data[0].id,500,350);
      }
    };
    //监听工具条
    table.on('tool(user)', function (obj) {
      var data = obj.data;
      if (obj.event === 'del') {
        layer.confirm('确定删除用户[<label style="color: #00AA91;">' + data.userName + '</label>]?',{ title:'提示'},
        function () {
          del(data.id);
        });
      } 
      else if (obj.event === 'edit') {
        update('编辑用户', 'showUpdateUser?id=' + data.id, 700, 450);
      }
    });

    $('.select .layui-btn').on('click', function () {
      var type = $(this).data('type');
      active[type] ? active[type].call(this) : '';
    });

  });

  function rePwd(title,url,w,h){
    if (title == null || title == '') {
      title = false;
    };
    if (url == null || url == '') {
      url = "404.html";
    };
    if (w == null || w == '') {
      w = ($(window).width() * 0.9);
    };
    if (h == null || h == '') {
      h = ($(window).height() - 50);
    };
    layer.open({
      id: 'user-rePwd',
      type: 2,
      area: [w + 'px', h + 'px'],
      fix: false,
      maxmin: true,
      shadeClose: true,
      shade: 0.4,
      title: title,
      content: url,
    });
  }
  function del(id) {
    $.ajax({
      url:"del",
      type:"post",
      data:{id:id},
      async:false,
      success:function(d){
        if(d.flag){
          window.top.layer.msg(d.msg,{icon:6,time:1000});
          layui.table.reload('userList');
        }else{ 
          window.top.layer.msg(d.msg);
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
      id: 'user-update',
      type: 2,
      area: [w + 'px', h + 'px'],
      fix: false,
      maxmin: true,
      shadeClose: false,
      shade: 0.4,
      title: title,
      content: url + '&detail=false'
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
      id: 'user-add',
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
