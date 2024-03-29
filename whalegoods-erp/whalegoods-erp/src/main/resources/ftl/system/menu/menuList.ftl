<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title></title>
  <link rel="stylesheet" href="${re.contextPath}/plugin/layuitree/layui/css/layui.css">
  <link rel="stylesheet" href="${re.contextPath}/plugin/erp/main.css"/>
</head>
<body>
<div  class="layui-col-md13">
    <div class="layui-btn-group">
      <@shiro.hasPermission name="menu:add">
        <button class="layui-btn layui-btn-normal" data-type="add">
            <i class="layui-icon">&#xe608;</i>新增
        </button>
      </@shiro.hasPermission>
    </div>
    <button class="layui-btn layui-btn-sm icon-position-no-button" id="refresh" style="float: right;" onclick="javascript:location.replace(location.href);">
      <i class="layui-icon i-icon" style="font-size: 21px">ဂ</i>
    </button>
</div>
<div id="menuTree"></div>
</body>
<script type="text/javascript" src="${re.contextPath}/plugin/layuitree/layui/layui.js" charset="utf-8"></script>
<script type="text/javascript">

  //添加函数
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

  //删除函数
  function del(nodeId) {
    console.info(nodeId);
    alert(nodeId)
  }
  
  //定义列表界面模版对象
  var layout = [
    { name: '菜单名称', treeNodes: true, headerClass: 'value_col', colClass: 'value_col', style: 'width: 10%',
        render: function(row) { 
      	  return '<div class="layui-table-cell laytable-cell-1-username">'+(typeof(row.menuName)=="undefined"?'未显示成功':row.menuName)+'</div>'; //列渲染
        }
    },
    { name: 'url',headerClass: 'value_col', colClass: 'value_col', style: 'width: 10%',
      render: function(row) { 
    	  return '<div class="layui-table-cell laytable-cell-1-username">'+(typeof(row.menuUrl)=="undefined"?'':row.menuUrl)+'</div>'; //列渲染
      }
    }, 
    { name: '类型',headerClass: 'value_col', colClass: 'value_col', style: 'width: 10%',
      render: function(row) {
        return '<div class="layui-table-cell laytable-cell-1-username">'+(row.menuType=="1"?'按钮':'菜单')+'</div>'; //列渲染
      }
    }, 
    { name: '权限',headerClass: 'value_col', colClass: 'value_col', style: 'width: 10%',
      render: function(row) {
        return '<div class="layui-table-cell laytable-cell-1-username">'+(typeof(row.permission)=="undefined"?'':row.permission)+'</div>'; //列渲染
      }
    },
    { name: '图标',headerClass: 'value_col', colClass: 'value_col', style: 'width: 5%',
      render: function(row) {
        return '<div class="layui-table-cell laytable-cell-1-username"><i class="layui-icon">'+(typeof(row.icon)=="undefined"?'':row.icon)+'</i></div>'; //列渲染
      }
    },
    { name: '序号',headerClass: 'value_col', colClass: 'value_col', style: 'width: 5%',
          render: function(row) {
              return '<div class="layui-table-cell laytable-cell-1-username"><i class="layui-icon">'+(typeof(row.orderNum)=="undefined"?'':row.orderNum)+'</i></div>'; //列渲染
          }
    },
    {
      name: '操作',
      headerClass: 'value_col',
      colClass: 'value_col',
      style: 'width: 20%',
      render: function(row) {
        var chil_len=row.children.length;
        var str= '<a class="layui-btn layui-btn-primary layui-btn-xs" onclick="del(\'' + row.id + '\')"><i class="layui-icon">&#xe615;</i> 查看</a>' +
            '<a class="layui-btn layui-btn-xs  layui-btn-normal" onclick="del(\'' + row.id + '\')"><i class="layui-icon">&#xe642;</i> 编辑</a>'; //列渲染
        if(chil_len==0){
          str+='<a class="layui-btn layui-btn-danger layui-btn-xs" onclick="del(\'' + row.id + '\')"><i class="layui-icon">&#xe640;</i> 删除</a>';
        }
        return str;
      }
    },
  ];

  layui.use(['tree', 'layer'], function() {
    var layer = layui.layer;
    layui.treeGird({
      elem: '#menuTree',
      nodes:${menus},
      layout: layout
    });
    var $ = layui.$, active = {
        add: function () {
            add('添加菜单', 'showAddMenu', 700, 450);
        }
    }
    $('.layui-btn-group .layui-btn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
  });

</script>

</html>