# Mysterious
```
分布式压测平台后端服务，提供性能相关的服务
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
前端服务：https://github.com/100ZZ/mysterious-web
<br>
Meter工具：https://github.com/100ZZ/mysterious-jmeter
<br>
数据库：MySQL，Redis，Nginx

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
      3. http://localhost:8888即可进入登录页面
   2. 如果是服务器部署
      1. 后端直接启动springboot
      2. 前端最好起个nginx（压测报告还是会用到web server），dist放在HTML里
      3. http://${server_ip}:8888即可进入登录页面
4. 登录页面，注册，输入用户名密码，可以成功登录并跳转到用户管理页面说明对接无误；因为密码后端加密，无法解密，能够isMatch校验，因此没法初始化插库
![login.png](https://github.com/100ZZ/mysterious/tree/master/pic/login.png)

<br>
后端服务应该没多少BUG，前端刚开始写，目前只有登录后的用户管理页面，所以暂时无法玩耍，大致流程如下

![img.png](https://github.com/100ZZ/mysterious/tree/master/pic/img.png)