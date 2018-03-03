<?php
header('Content-type: application/json; charset=UTF-8');
// 设置REST API调用基本参数
$sdkappid = 0;
$identifier = "admin";
$private_key_path = "/opt/lampp/htdocs/petpet/tim/private.pem";
$signature = "/opt/lampp/htdocs/petpet/tim/signature/linux-signature64";
$usersig = '';//设置管理员usersig	180天失效
