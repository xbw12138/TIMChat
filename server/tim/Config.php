<?php
header('Content-type: application/json; charset=UTF-8');
// 设置REST API调用基本参数
$sdkappid = 0;
$identifier = "admin";
$private_key_path = dirname(__FILE__)."/private.pem";
$signature = dirname(__FILE__)."/signature/linux-signature64";
$usersig = '';//设置管理员usersig	180天失效
