# Mysterious
```
分布式压测平台后端服务，纯SpringBoot服务，提供性能相关的服务
```
<br>

**依赖**
```
后端服务：https://github.com/100ZZ/mysterious
前端服务：https://github.com/100ZZ/mysterious-web
JMeter工具包：https://github.com/100ZZ/mysterious-jmeter
其它组件：MySQL，Redis，Nginx
```

<br> 

**服务部署**
<br>
Docker-Compose部署方式
```html
A. 编译前端
   1. git clone https://github.com/100ZZ/mysterious-web
   2. cd mysterious-web
   3. npm install
   4. npm run build (生成dist)
B. 部署服务
   1. https://github.com/100ZZ/mysterious
   2. cd mysterious/docker
   3. ./init.sh (包括创建base目录默认/opt/mysterious，初始化sql，nginx配置，下载jmeter包等，如果有报错，看shell脚本定位)
   4. cp [上一步前端目录]/dist /opt/mysterious/nginx/html/
   5. docker-compose --env-file VERSION.env up -d
   6. docker-compose ps查看，有mysterious，mysterious-mysql，mysterious-nginx，mysterious-redis四个容器
C. 访问平台
   1. http://xx.xx.xx.xx:1234
   2. 注册个用户，登录即可玩耍
```
二进制部署方式
```html
A. 编译前端
   1. git clone https://github.com/100ZZ/mysterious-web
   2. cd mysterious-web
   3. npm install
   4. npm run build (生成dist)
B. 安装nginx
   1. 待续
C. 安装mysql
   1. 待续
D. 安装redis
   1. 待续
E. 部署后端
   1. 待续
```
4. Slave节点部署
如果需要用分布式压测，需要部署Slave节点
```html
A. 待续
```
<br>

**经验总结**
<br>
[JMeter分布式压测](https://lihuia.com/jmeter%e5%88%86%e5%b8%83%e5%bc%8f%e5%8e%8b%e6%b5%8b/)
<br>
[JMeter有关JAR依赖异常问题](https://lihuia.com/jmeter%e6%9c%89%e5%85%b3jar%e4%be%9d%e8%b5%96%e7%9a%84%e9%97%ae%e9%a2%98/)
<br>
[吐血定位端口映射影响JMeter分布式压测的异常问题](https://lihuia.com/%e5%90%90%e8%a1%80%e5%ae%9a%e4%bd%8d%e7%ab%af%e5%8f%a3%e6%98%a0%e5%b0%84%e5%bd%b1%e5%93%8djmeter%e5%88%86%e5%b8%83%e5%bc%8f%e5%8e%8b%e6%b5%8b%e7%9a%84%e5%bc%82%e5%b8%b8%e9%97%ae%e9%a2%98/)
<br>
[JMeter分布式压测启动流程简述](https://lihuia.com/jmeter%e5%88%86%e5%b8%83%e5%bc%8f%e5%8e%8b%e6%b5%8b%e5%90%af%e5%8a%a8%e6%b5%81%e7%a8%8b%e7%ae%80%e8%bf%b0/)
<br>
[JMeter分布式平台化相关的异常问题汇总](https://lihuia.com/jmeter%e5%88%86%e5%b8%83%e5%bc%8f%e7%9b%b8%e5%85%b3%e7%9a%84%e5%bc%82%e5%b8%b8%e9%97%ae%e9%a2%98%e6%b1%87%e6%80%bb/)
<br>
[分布式压测平台响应时间的损耗分析](https://lihuia.com/%e5%8e%8b%e6%b5%8b%e5%b9%b3%e5%8f%b0%e5%93%8d%e5%ba%94%e6%97%b6%e9%97%b4%e7%9a%84%e6%8d%9f%e8%80%97%e5%88%86%e6%9e%90/)
<br>
[平台化：JMeter脚本在线编辑初步实现](https://lihuia.com/%e5%b9%b3%e5%8f%b0%e5%8c%96%ef%bc%9ajmeter%e8%84%9a%e6%9c%ac%e5%9c%a8%e7%ba%bf%e7%bc%96%e8%be%91%e5%88%9d%e6%ad%a5%e5%ae%9e%e7%8e%b0/)
