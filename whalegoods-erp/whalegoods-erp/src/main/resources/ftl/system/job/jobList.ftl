<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>任务列表</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=job-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
  <link rel="stylesheet" href="${re.contextPath}/plugin/layui/css/layui.css">
  <link rel="stylesheet" href="${re.contextPath}/plugin/erp/main.css">
  <script type="text/javascript" src="${re.contextPath}/plugin/jquery/jquery-3.2.1.min.js"></script>
  <script type="text/javascript" src="${re.contextPath}/plugin/layui/layui.all.js" charset="utf-8"></script>
</head>

<body>
<div class="erp-search">
  <div class="select">
    <@shiro.hasPermission name="job:add"> <button class="layui-btn layui-btn-normal layui-btn-sm" data-type="add"><i class="layui-icon">&#xe608;</i>新增</button></@shiro.hasPermission>
    <@shiro.hasPermission name="job:update"><button class="layui-btn  layui-btn-sm" data-type="update"><i class="layui-icon">&#xe642;</i>编辑</button></@shiro.hasPermission>
    <button class="layui-btn layui-btn-sm icon-position-button" id="refresh" style="float: right;" data-type="reload"><i class="layui-icon">&#x1002;</i></button>
  </div>
</div>
<table id="jobList" class="layui-hide" lay-filter="job"></table>
<script type="text/html" id="rightToolBar">
<@shiro.hasPermission name="job:del"><a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a></@shiro.hasPermission>
</script>
<script type="text/html" id="tplJobStatus">
<input type="checkbox" name="jobStatus" value="{{d.id}}" lay-skin="switch" lay-text="开启|关闭" lay-filter="radioJobStatus" {{ d.jobStatus == 1 ? 'checked' : '' }}>
</script>
<script type="text/html" id="tplSwitchStatus">
<input type="checkbox" name="switchStatus" value="{{d.id}}" lay-skin="switch" lay-text="开启|关闭" lay-filter="radioSwitchStatus" {{ d.switchStatus == 1 ? 'checked' : '' }}>
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
      id: 'jobList',
      elem: '#jobList',  
      url: 'showJobList',
      cols: [[
        {checkbox: true, fixed: true},
        {field: 'jobName',title: '任务名称',align:'center' }, 
        {field: 'jobCron', title: 'Cron表达式', align:'center'},
        {field: 'jobStatus', title: '任务启动状态',templet: '#tplJobStatus',align:'center'},
        {field: 'execPath', title: '执行路径', align:'center'},
        {field: 'switchStatus',title: '通知开关状态',align:'center',templet: '#tplSwitchStatus', unresize: true},
        {field: 'jobDesc', title: '任务描述', align:'center'},
        {field: 'right', title: '操作',align:'center', toolbar: "#rightToolBar"}
      ]],
      page: true,
      height: 'full-46'
    });
    
    form.on('switch(radioJobStatus)', function(obj){
        $.ajax({
            url:"updateJobStatus",
            type:"post",
            data:{id:this.value,jobStatus:obj.elem.checked},
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
    
    form.on('switch(radioSwitchStatus)', function(obj){
        $.ajax({
            url:"updateSwitchStatus",
            type:"post",
            data:{id:this.value,switchStatus:obj.elem.checked},
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
      reload:function(){
        table.reload('jobList', {
          where: {
          }
        });
      },
      add: function () {
        add('添加任务', 'showAddJob', 600, 500);
      },
      update: function () {
        var checkStatus = table.checkStatus('jobList'), data = checkStatus.data;
        if (data.length != 1) {
          layer.msg('请选择一行编辑,已选['+data.length+']行', {icon: 5,time:1000});
          return false;
        }
        update('更新任务', 'showUpdateJob?id=' + data[0].id, 600, 500);
      }
    };
    //监听工具条
    table.on('tool(job)', function (obj) {
      var data = obj.data;
      if (obj.event === 'del') {
        layer.confirm('确定删除：[<label style="color: #00AA91;">' + data.jobName+ '</label>]这个任务?',{ title:'提示'},
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
      url:"delJob",
      type:"post",
      data:{id:id},
      async:false,
      success:function(d){
        if(d.result_code==0){
          window.top.layer.msg(d.result_msg,{icon:6,time:1000});
          layui.table.reload('jobList');
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
      id: 'job-update',
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
      id: 'job-add',
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
