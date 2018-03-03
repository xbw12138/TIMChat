<?php
require_once("TimRestApi.php");
require_once("Config.php");

$api = createRestAPI();
$api->init($sdkappid, $identifier);
$userid=$_GET['userid'];
$ret = $api->generate_user_sig($userid, '86400',$private_key_path, $signature);
if ($ret == null)
{
    // 签名生成失败
    return -10;
}
echo $ret[0];