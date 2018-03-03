<?php
require_once("TimRestApi.php");
require_once("Config.php");

$api = createRestAPI();
$api->init($sdkappid, $identifier);
$api->set_user_sig($usersig);
$userid=$_GET['userid'];
$ret = $api->account_import($userid, "pet", "");