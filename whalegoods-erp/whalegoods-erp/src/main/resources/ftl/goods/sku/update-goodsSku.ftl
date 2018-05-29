<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>添加商品</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi" />
  <link rel="stylesheet" href="${re.contextPath}/plugin/layui/css/layui.css">
  <script type="text/javascript" src="${re.contextPath}/plugin/jquery/jquery-3.2.1.min.js"></script>
  <script type="text/javascript" src="${re.contextPath}/plugin/layui/layui.all.js" charset="utf-8"></script>
</head>

<body>
<div class="x-body" style="width:800px;height:950px">
  <form class="layui-form layui-form-pane" style="margin: 20px;">
    <div class="layui-form-item">
    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;"><legend style="font-size:16px;">商品图片上传</legend></fieldset>
     <div class="layui-input-inline">
    <div class="layui-upload-drag" style="margin-left:10%;" id="upPic">
      <i style="font-size:30px;" class="layui-icon"></i>
     <p style="font-size: 10px">点击上传，或将文件拖拽到此处</p>     
    </div>
      </div>
      <div class="layui-input-inline">
          <div  id="upPicNow" style="margin-top: 20px;margin-left: 50px">
            <img src="${goodsSku.picUrl}" width="100px" height="100px" class="layui-upload-img">
          </div>

      </div>
    </div>
    <div class="layui-form-item">
      <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;"><legend style="font-size:16px;">商品信息</legend></fieldset>
    </div>
    <!-- 商品编码 -->
    <div class="layui-form-item">
    <input value="${goodsSku.id}" type="hidden" name="id">
    <input type="hidden" id="hidPicUrl" name="picUrl" value="${goodsSku.picUrl}" />
      <label for="goodsCode" class="layui-form-label"><span class="x-red">*</span>商品编码</label>
      <div class="layui-input-inline"><input type="text"  id="goodsCode" value="${goodsSku.goodsCode}" name="goodsCode"  lay-verify="required" autocomplete="off" class="layui-input"></div>
      <div id="ms" class="layui-form-mid layui-word-aux"><span class="x-red">*</span><span id="ums">商品编码唯一</span></div>
    </div>
    
    <div class="layui-form-item">
    <div class="layui-inline">
    <!-- 中文品名-->
    <label for="goodsName" class="layui-form-label"><span class="x-red">*</span>中文品名</label>
      <div class="layui-input-inline"><input type="text"  id="goodsName" value="${goodsSku.goodsName}" name="goodsName"  lay-verify="required" autocomplete="off" class="layui-input"></div>
    </div>
    <div class="layui-inline">
    <!--英文品名-->
      <label for="goodsNameEn" class="layui-form-label">英文品名</label>
      <div class="layui-input-inline"><input type="text"  id="goodsNameEn" value="${goodsSku.goodsNameEn}" name="goodsNameEn"  autocomplete="off" class="layui-input"></div>
       </div>
    </div>
         
    <div class="layui-form-item">
     <div class="layui-inline">
     <!--商品规格-->
     <label for="spec" class="layui-form-label">商品规格</label>
      <div class="layui-input-inline"><input type="text"  id="spec" value="${goodsSku.spec}" name="spec"  autocomplete="off" class="layui-input"></div>
     </div>
     <div class="layui-inline">
     <!--箱包装数-->
     <label for="boxNum" class="layui-form-label"><span class="x-red">*</span>箱包装数</label>
      <div class="layui-input-inline"><input type="text"  id="boxNum" value="${goodsSku.boxNum}" name="boxNum" lay-verify="required|number"  autocomplete="off" class="layui-input"></div>
     </div>
    </div>
     
    <div class="layui-form-item">
    <div class="layui-inline">
    <!--单个成本价-->
    <label for="oneCost" class="layui-form-label"><span class="x-red">*</span>单个成本价</label>
      <div class="layui-input-inline"><input type="text"  id="oneCost" value="${goodsSku.oneCost}" name="oneCost" lay-verify="required|number"  placeholder="￥"  autocomplete="off" class="layui-input"></div>
     </div>
     <div class="layui-inline">
     <!--建议零售价-->
     <label for="marketPrice" class="layui-form-label"><span class="x-red">*</span>建议零售价</label>
      <div class="layui-input-inline"><input type="text"  id="marketPrice" value="${goodsSku.marketPrice}" name="marketPrice" lay-verify="required|number"  placeholder="￥"  autocomplete="off" class="layui-input"></div>
     </div>
      
    </div>

    <div class="layui-form-item">
    <div class="layui-inline">
     <!--生产日期-->
     <label for="productDate" class="layui-form-label"><span class="x-red">*</span>生产日期</label>
      <div class="layui-input-inline"><input type="text"  id="productDate" value="${goodsSku.productDate?string('yyyy-MM-dd')}" name="productDate" placeholder="年-月-日" lay-verify="required|date"  autocomplete="off" class="layui-input"></div>
    </div>
    <div class="layui-inline">
     <!--保质期，按月数-->
     <label for="shelfLife" class="layui-form-label"><span class="x-red">*</span>保质期</label>
      <div class="layui-input-inline"><input type="text"  id="shelfLife" value="${goodsSku.shelfLife}" name="shelfLife" lay-verify="required|number|shelfLife"  autocomplete="off" class="layui-input"></div>
    </div>
     
    </div>
    <!-- 原产地 -->
    <div class="layui-form-item">
      <label for="madeIn" class="layui-form-label">原产地</label>
      <div class="layui-input-inline"><input type="text" value="${goodsSku.madeIn}" id="madeIn" name="madeIn"  autocomplete="off" class="layui-input"></div>
    </div>
    <!-- 商品详情 -->
    <div class="layui-form-item layui-form-text">
      <label for="goodsDetail" class="layui-form-label">商品详情</label>
      <div class="layui-input-block">
      <textarea placeholder="请输入商品详情" class="layui-textarea" name="goodsDetail" >${goodsSku.goodsDetail}</textarea>
    </div>
    </div>
    <!-- 商品详情英文 -->
    <div class="layui-form-item layui-form-text">
      <label for="goodsDetailEn" class="layui-form-label">英文介绍</label>
      <div class="layui-input-block">
      <textarea placeholder="please input english description" class="layui-textarea" id="goodsDetailEn" name="goodsDetailEn" >${goodsSku.goodsDetailEn}</textarea>
    </div>
    </div>
  <div style="width: 100%;height: 55px;background-color: white;border-top:1px solid #e6e6e6; position: fixed;bottom: 1px;margin-left:-20px;">
    <div class="layui-form-item" style=" float: right;margin-right: 30px;margin-top: 8px">
      <button  class="layui-btn layui-btn-normal" lay-filter="confirm" lay-submit="">确定</button>
      <button  class="layui-btn layui-btn-primary" id="close">取消</button>
    </div>
  </div>
  </form>
</div>
<script>
var flag,msg;
$(function(){
    $('#goodsCode').on("blur",function(){
      var goodsCode=$('#goodsCode').val();
      if(goodsCode!='') {
        $.ajax({
          url: 'checkGoodsCode?goodsCode=' + goodsCode, 
          async: false, 
          type: 'get', 
          success: function (data) {
            flag = data.result_code;
            $('#ms').find('span').remove();
            if (!data.result_code==0) {
              msg = data.result_msg;
              $('#ms').append("<span style='color: red;'>"+data.result_msg+"</span>");
            }else{
              $('#ms').append("<span style='color: green;'>商品编码可用</span>");
            }},
            beforeSend:function(){
            $('#ms').find('span').remove();
            $('#ms').append("<span>验证ing</span>");
          }
        });
      }
    });
});

layui.use(['form','layer','upload','laydate'], function(){
  $ = layui.jquery;
  var form = layui.form,layer = layui.layer,upload = layui.upload,laydate = layui.laydate;
  
  laydate.render({
      elem: '#productDate'
      ,max:0
    });
  
  upload.render({
    elem: '#upPic',
    url: 'uploadGoodsPic',
    before: function(obj){
      //预读，不支持ie8
      obj.preview(function(index, headPicUrl, result){
        $('#upPicNow').find('img').remove();
        $('#upPicNow').append('<img src="'+ result +'" alt="'+ headPicUrl.name +'" width="100px" height="100px" class="layui-upload-img">');
      });
    },
    done: function(res){
     if(!res.flag){
       layer.msg(res.msg,{icon: 5});
     }
     else{
  	   $('#hidPicUrl').val(res.data.file_url);
     }
    }
  });

  //自定义验证规则
  form.verify({
  	shelfLife: function(value){
      if(!/^[0-9]+$/.test(value)){
        return "保质期（按月）必须为正整数";
      }
  }});

 $('#close').click(function(){
   var index = parent.layer.getFrameIndex(window.name);
   parent.layer.close(index);
 });
 
    //监听提交
    form.on('submit(confirm)', function(data){
      $.ajax({
        url:'updateGoodsSku',
        type:'post',
        data:data.field,
        async:false,
        contentType : 'application/json',  
        data:JSON.stringify(data.field),
        traditional: true,
        success:function(d){
            var index = parent.layer.getFrameIndex(window.name);
          parent.layer.close(index);
          window.parent.layui.table.reload('skuList');
          window.top.layer.msg(d.result_msg,{icon:6,time:1000});
        },
        error:function(){
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
          window.top.layer.msg('请求失败');
        }
      });
      return false;
    });
    form.render();
  });
</script>
</body>

</html>
