<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <title>鲸品智能后台管理系统</title>
  <link rel="stylesheet" href="${re.contextPath}/plugin/plugins/layui/css/layui.css" media="all" />
  <link rel="stylesheet" href="${re.contextPath}/plugin/plugins/font-awesome/css/font-awesome.min.css" media="all" />
  <link rel="stylesheet" href="${re.contextPath}/plugin/build/css/app.css" media="all" />
  <link rel="stylesheet" href="${re.contextPath}/plugin/build/css/themes/default.css" media="all" id="skin" kit-skin />
  <style>
    <#--前端无聊美化ing-->
    .layui-footer{background-color: #2F4056;}
    .layui-side-scroll{border-right: 3px solid #009688;}
  </style>
</head>

<body class="kit-theme">
<div class="layui-layout layui-layout-admin kit-layout-admin">
  <div class="layui-header">
    <div class="layui-logo">鲸品智能后台管理系统</div>
    <div class="layui-logo kit-logo-mobile"></div>
<!--     <div class="layui-hide-xs">
    <ul class="layui-nav layui-layout-left kit-nav">
      <li class="layui-nav-item">
        <a href="javascript:;">其它系统</a>
        <dl class="layui-nav-child">
          <dd><a href="javascript:;">邮件管理</a></dd>
          <dd><a href="javascript:;">消息管理</a></dd>
          <dd><a href="javascript:;">授权管理</a></dd>
        </dl>
      </li>
    </ul>
    </div> -->
    <ul class="layui-nav layui-layout-right kit-nav">
      <li class="layui-nav-item">
        <a href="javascript:;">
        <#assign currentUser = Session["currentUser"]>
          欢迎你，${currentUser.username}
        </a>
      </li>
      <li class="layui-nav-item"><a href="/logout"><i class="fa fa-sign-out" aria-hidden="true"></i> 注销</a></li>
    </ul>
  </div>

<#macro tree data start end>
  <#if (start=="start")>
  <div class="layui-side layui-nav-tree layui-bg-black kit-side">
  <div class="layui-side-scroll">
    <div class="kit-side-fold"><i class="fa fa-navicon" aria-hidden="true"></i></div>
  <ul class="layui-nav layui-nav-tree" lay-filter="kitNavbar" kit-navbar>
  </#if>
  <#list data as child>
    <#if child.children?size gt 0>
      <li class="layui-nav-item">
        <a class="" href="javascript:;"><i aria-hidden="true" class="layui-icon">${child.icon}</i><span> ${child.name}</span></a>
        <dl class="layui-nav-child">
          <@tree data=child.children start="" end=""/>
        </dl>
      </li>
    <#else>
      <dd>
        <a href="javascript:;" kit-target data-options="{url:'${child.menuUrl}',icon:'${child.icon}',title:'${child.name}',id:'${child.num?c}'}">
          <i class="layui-icon">${child.icon}</i><span> ${child.name}</span></a>
      </dd>
    </#if>
  </#list>
  <#if (end=="end")>
  </ul>
  </div>
  </div>
  </#if>
</#macro>
<@tree data=menu start="start" end="end"/>
  <div class="layui-body" id="container">
    <!-- 内容主体区域 -->
    <div style="padding: 15px;"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63e;</i> 请稍等...</div>
</div>
<script src="${re.contextPath}/plugin/plugins/layui/layui.js"></script>
<script src="${re.contextPath}/plugin/tools/main.js"></script>
</body>

</html>
