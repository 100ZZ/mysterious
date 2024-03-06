# Mysterious
> 分布式压测平台后端服务，前端Vue3+TypeScript，后端SpringBoot服务，压测引擎JMeter5.5，基础是JMeter的分布式压测

## 经验总结
>- [1. JMeter分布式压测](https://lihuia.com/jmeter%e5%88%86%e5%b8%83%e5%bc%8f%e5%8e%8b%e6%b5%8b/)
>- [2. JMeter有关JAR依赖异常问题](https://lihuia.com/jmeter%e6%9c%89%e5%85%b3jar%e4%be%9d%e8%b5%96%e7%9a%84%e9%97%ae%e9%a2%98/)
>- [3. 吐血定位端口映射影响JMeter分布式压测的异常问题](https://lihuia.com/%e5%90%90%e8%a1%80%e5%ae%9a%e4%bd%8d%e7%ab%af%e5%8f%a3%e6%98%a0%e5%b0%84%e5%bd%b1%e5%93%8djmeter%e5%88%86%e5%b8%83%e5%bc%8f%e5%8e%8b%e6%b5%8b%e7%9a%84%e5%bc%82%e5%b8%b8%e9%97%ae%e9%a2%98/)
>- [4. JMeter分布式压测启动流程简述](https://lihuia.com/jmeter%e5%88%86%e5%b8%83%e5%bc%8f%e5%8e%8b%e6%b5%8b%e5%90%af%e5%8a%a8%e6%b5%81%e7%a8%8b%e7%ae%80%e8%bf%b0/)
>- [5. JMeter分布式平台化相关的异常问题汇总](https://lihuia.com/jmeter%e5%88%86%e5%b8%83%e5%bc%8f%e7%9b%b8%e5%85%b3%e7%9a%84%e5%bc%82%e5%b8%b8%e9%97%ae%e9%a2%98%e6%b1%87%e6%80%bb/)
>- [6. 分布式压测平台响应时间的损耗分析](https://lihuia.com/%e5%8e%8b%e6%b5%8b%e5%b9%b3%e5%8f%b0%e5%93%8d%e5%ba%94%e6%97%b6%e9%97%b4%e7%9a%84%e6%8d%9f%e8%80%97%e5%88%86%e6%9e%90/)
>- [7. 平台化：JMeter脚本在线编辑初步实现](https://lihuia.com/%e5%b9%b3%e5%8f%b0%e5%8c%96%ef%bc%9ajmeter%e8%84%9a%e6%9c%ac%e5%9c%a8%e7%ba%bf%e7%bc%96%e8%be%91%e5%88%9d%e6%ad%a5%e5%ae%9e%e7%8e%b0/)

## 平台依赖
>- 后端服务：https://github.com/100ZZ/mysterious
>- 前端服务：https://github.com/100ZZ/mysterious-web
>- JMeter工具包：https://github.com/100ZZ/mysterious-jmeter
>- 其它组件：MySQL，Redis，Nginx

## 安装部署
### Docker-Compose部署方式（Master节点）
_容器化部署比较简单，但Master节点需要部署一堆Docker的组件，会有一些资源占用的损耗，本身是性能测试，所以如果是对压测数据要求比较高，也可以二进制单节点部署_
<br>
_视频说明：https://www.bilibili.com/video/BV1by421i7cn_
1. 前端部署
>- git clone https://github.com/100ZZ/mysterious-web.git
>- cd mysterious-web
>- npm install
>- npm run build (生成dist)

2. 平台部署
>- git clone https://github.com/100ZZ/mysterious.git
>- cd mysterious/docker
>- ./init.sh (如果有报错，看shell脚本每一步操作，来定位)
>- cp -r [上一步前端目录]/dist /opt/mysterious/nginx/html/
>- docker-compose --env-file VERSION.env up -d
>- docker-compose ps查看，有mysterious，mysterious-mysql，mysterious-nginx，mysterious-redis四个容器
3. 访问平台
>- http://xx.xx.xx.xx:1234
>- 注册个用户，登录即可玩耍
### 二进制部署方式（Master节点）
_比较推荐弄一个高配额的单节点来部署平台进行压测，避免了Docker和分布式交互的开销，下面以CentOS7为例介绍下安装步骤_
1. 前端部署
>- git clone https://github.com/100ZZ/mysterious-web.git
>- cd mysterious-web
>- npm install
>- npm run build (生成dist)
   
2. 安装nginx，mysql，redis
> nginx
>- 1234.conf和9998.conf复制到/etc/nginx/conf.d下（根据系统实际情况），并修改下，比如mysterious-nginx改成localhost，mysterious改成最后前端页面访问的IP地址
>- mkdir -p /usr/share/nginx/html/
>- cp -r [上一步前端目录]/dist /usr/share/nginx/html/

> mysql
>- mysql> ALTER USER 'root'@'localhost' IDENTIFIED BY 'Test@123456';
>- mysql> use mysql;
>- mysql> update user set host='%' where user='root';

> redis
>- 没啥好说的，起来就行了，不用设密码
>- redis.conf可自行下载，或者docker目录下有
   
3. 平台部署
>- git clone https://github.com/100ZZ/mysterious.git
>- 修改application.properties，mysterious-mysql和mysterious-redis都改成localhost
>- mvn -f pom.xml clean install package -Dmaven.test.skip=true
>- mkdir -p /opt/mysterious/mysterious-data
>- mkdir -p /opt/mysterious/running
>- cp docker/mysterious.jar /opt/mysterious/running/
>- cp docker/service.sh /opt/mysterious/running/
>- cd /opt/mysterious
>- git clone https://github.com/100ZZ/mysterious-jmeter.git
>- sh /opt/mysterious/running/service.sh restart
4. 访问平台
>- http://xx.xx.xx.xx:1234
>- 注册个用户，登录即可玩耍

### Slave节点部署
_如果需要分布式部署，找到和Master节点网络互通的节点，最好是局域网，否则网络开销太大；无论Master节点是二进制还是Docker-Compose部署，Slave节点部署方式都如下_
>- mkdir /opt/mysterious
>- cd /opt/mysterious
>- git clone https://github.com/100ZZ/mysterious-jmeter.git
>- 如果之前有一些测试用例，可以页面节点管理，先点击一下节点同步，会将master节点用例数据都同步到slave节点，然后启用slave节点即可
>- 只要有slave节点启用，压测都会是分布式压测，如果全都禁用，压测就只是Master单节点压测


## 说明
_整个安装部署包括平台的前后端服务和JMeter工具包目录_
>- 如果是单节点压测，只需要在一个Master节点上安装部署压测平台和Jmeter工具包，通过平台来调用Jmeter来执行压测用例，并返回压测结果
>- 如果是分布式压测，除了上面Master节点安装部署外，还要在Slave节点上安装Jmeter工具包，启动jmeter-server进程，大致数据流如下
   ![分布式压测](https://lihuia.com/wp-content/uploads/2020/12/UntitledImage.png)
   <br>
