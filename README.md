# Mysterious
```
分布式压测平台后端服务，纯SpringBoot服务，提供性能相关的服务
```
<br> 

**接口文档**
```
本地环境：http://localhost:9999/swagger-ui.html
接口脚本：api/api.jmx
```
<br>

**依赖**
<br>
后端服务：https://github.com/100ZZ/mysterious
<br>
前端服务：https://github.com/100ZZ/mysterious-web
<br>
JMeter工具包：https://github.com/100ZZ/mysterious-jmeter
<br>
数据库：MySQL，Redis，Nginx

<br> 

**启动服务**
<br>
1. 目录选取（建议创建一个mysterious文件夹，供下面子文件夹并列存放）
   1. mysterious-jmeter（jmeter执行包）    
   2. mysterious-data（存放testcase相关jmx，csv，jar，report的）
2. 数据库初始化
   1. create datebase mysterious
   2. 建表，执行schema.sql
   3. 初始化数据，执行insert.sql，也可以后续页面创建配置（路径就是1中的目录）
3. 前后端分离，已经是跨域
   1. 如果是本地调试
      1. 后端直接启动springboot
      2. 前端npm install；npm run dev
      3. http://localhost:7777 即可进入登录页面
   2. 如果是服务器部署
      1. 后端直接启动springboot
      2. 前端最好起个nginx（压测报告还是会用到web server），dist放在HTML里
      3. http://${server_ip}:7777 即可进入登录页面
4. 登录页面，注册，输入用户名密码，可以成功登录并跳转到用户管理页面说明对接无误；因为密码后端加密，无法解密，能够isMatch校验，因此没法初始化插库
![image](https://raw.githubusercontent.com/100ZZ/mysterious/master/pic/login.png)

**进度**
<br>
后端服务应该没多少BUG，前端还没写完（第一次玩），目前功能还没实现完，所以暂时无法玩耍，大致流程如下

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
