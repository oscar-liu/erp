<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>二维码</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi" />
  <script type="text/javascript" src="${re.contextPath}/plugin/jquery/jquery-3.2.1.min.js"></script>
  <script type="text/javascript" src="${re.contextPath}/plugin/qrcode/jquery.qrcode.min.js"></script>
</head>

<body>
<div id="divQrCode">
  
</div>
<script>
  $(function(){
	  var qrCodeUrl=decodeURIComponent("${qrCodeUrl}");
	  $('#divQrCode').qrcode(qrCodeUrl);
  });
</script>
</body>

</html>
