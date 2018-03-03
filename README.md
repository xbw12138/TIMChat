# 腾讯云通信 独立模式

腾讯云通信android端 独立模式集成demo

## 服务器端

采用PHP SDK<br>

把server文件夹上传至服务器，<br>
修改server/tim/comfig.php配置<br>
修改private.pem为自己从腾讯云通讯控制台下载的私钥<br>

导入独立账号api为<br>
服务器IP+"/server/tim/apiimport.php";<br>

获取用户usersig api为<br>
服务器IP+"/server/tim/apigetsig.php";<br>

## 安卓端

采用SDK v3.x<br>

导入android文件夹到android studio<br>

修改Config.java配置信息<br>

到此配置结束，运行即可<br>


-------






