## 云服务器常用命令

* 查看用户密码
```
sudo cat /etc/shadow | grep [user]
```
* 查看进程id
```
$ ps -ef | grep java
$ kill -9 [pid]
```
* 配置密钥登录,客户端生成ssh公私钥分别对应[默认]id_rsa/id_rsa.pub
```
1、编辑ssh config支持密钥登录
$ vim /etc/ssh/sshd_config
2、内容打开以下注释
PubkeyAuthentication yes
3、将公钥文件上传到服务器后，通过命令将公钥写入此文件
$ cat id_rsa.pub >> ~/.ssh/authorized_keys
4、设置权限
$ chown user:[root] ~/.ssh/authorized_keys
$ chmod 600 ~/.ssh/authorized_keys
```
* 查找占用端口的进程 
```
sudo lsof -i :8080
```
* 查找并杀掉进程
```
kill -9 `lsof -t -i:8080`
```
