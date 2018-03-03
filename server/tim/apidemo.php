<?php
require_once("TimRestApi.php");
require_once("Config.php");

$api = createRestAPI();
$api->init($sdkappid, $identifier);
$api->set_user_sig($usersig);
$fromuser=$_GET['fromuser'];
$touser=$_GET['touser'];
$content=$_GET['content'];
$ret = $api->openim_send_msg($fromuser,$touser,$content);