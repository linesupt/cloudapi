# 云服务器前后端配置说明

### 在宝塔面板部署jar

```
java项目管理->项目信息->Spring_boot
项目jar路径: /project/cloud.jar
项目名称: cloud
项目端口: 8080
项目JDK: JDK[/usr/bin/java]
项目执行命令: 
/usr/bin/java -jar -Xmx1024M -Xms256M  /project/cloud.jar --server.port=8080
项目用户: www
项目备注: cloud
勾选开机启动
```

### nginx所在目录
```
/www/server/nginx/conf/nginx.conf
```

### linecloud前端页面部署所在目录(配置文件不需要内容)
```
/www/server/nginx/linecloud
/www/server/nginx/conf/linecloud.conf
```

### cloud.jar放置目录
```
/project/cloud.jar
```

### nginx.conf server 关键配置
```
server {  
listen 80;  
server_name 43.142.4.80;  # 替换为您的域名或IP地址  
root /www/server/nginx/linecloud;  # 替换为您的项目路径  
index index.html;

    location / {
    }  
    
    location /cloud {
        # 这里配置您的项目访问路径
        proxy_pass http://localhost:8080/cloud;
        #保留代理之前的host 包含客户端真实的域名和端口号
        proxy_set_header    Host  $host;
        #保留代理之前的真实客户端ip
        proxy_set_header    X-Real-IP  $remote_addr;
        #这个Header和X-Real-IP类似，但它在多级代理时会包含真实客户端及中间每个代理服务器的IP
        proxy_set_header    X-Forwarded-For  $proxy_add_x_forwarded_for;
        #表示客户端真实的协议（http还是https）
        proxy_set_header X-Forwarded-Proto $scheme;
    }
    
    location /bt {
        # 这里配置您的项目访问路径
        proxy_pass http://localhost:8888;
    }
}
```
### IDEA 插件自动部署
* 下载插件Alibaba Cloud Kit
* Settings -> Plugins -> Marketplace -> 搜索插件名称，安装后重启
配置部署信息
* Edit Configurations -> Deploy to host
1. 设置项目名称
2. 选择发布方式 Deployment -> Maven Build
3. 添加主机信息：设置IP、登录用户，密码，直到测试连接正常
4. 设置target打包后上传文件夹，文件夹不存在则会自动创建
5. 设置上传完成后执行的命令,自行指定端口
```
# 此句为先关闭对应项目端口在运行,以下多个命令需要用";"分割
$ kill -9 `lsof -t -i:8080`
$ /usr/bin/java -jar -Xmx1024M -Xms256M  /project/dev/cloud.jar --server.port=8081
```
6. [可选] 自行配置nginx项目路径

* 如果端口被占用需要先杀掉该端口的进程
* 关闭Java进程[端口]
```
先查询使用的进程
$ ps -ef | grep java
$ kill -9 [pid]
```




