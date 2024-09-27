# Mysterious
分布式压测平台，前端Vue3+TypeScript，后端SpringBoot-2.3.12，压测引擎JMeter-5.5，支持JMeter的分布式压测，管理，执行，报告，日志等。

https://github.com/user-attachments/assets/b951548a-7d16-4f0e-9d3e-7d3f1172a85f

## 经验总结
- [1. JMeter分布式压测](https://lihuia.com/jmeter%e5%88%86%e5%b8%83%e5%bc%8f%e5%8e%8b%e6%b5%8b/)
- [2. JMeter有关JAR依赖异常问题](https://lihuia.com/jmeter%e6%9c%89%e5%85%b3jar%e4%be%9d%e8%b5%96%e7%9a%84%e9%97%ae%e9%a2%98/)
- [3. 吐血定位端口映射影响JMeter分布式压测的异常问题](https://lihuia.com/%e5%90%90%e8%a1%80%e5%ae%9a%e4%bd%8d%e7%ab%af%e5%8f%a3%e6%98%a0%e5%b0%84%e5%bd%b1%e5%93%8djmeter%e5%88%86%e5%b8%83%e5%bc%8f%e5%8e%8b%e6%b5%8b%e7%9a%84%e5%bc%82%e5%b8%b8%e9%97%ae%e9%a2%98/)
- [4. JMeter分布式压测启动流程简述](https://lihuia.com/jmeter%e5%88%86%e5%b8%83%e5%bc%8f%e5%8e%8b%e6%b5%8b%e5%90%af%e5%8a%a8%e6%b5%81%e7%a8%8b%e7%ae%80%e8%bf%b0/)
- [5. JMeter分布式平台化相关的异常问题汇总](https://lihuia.com/jmeter%e5%88%86%e5%b8%83%e5%bc%8f%e7%9b%b8%e5%85%b3%e7%9a%84%e5%bc%82%e5%b8%b8%e9%97%ae%e9%a2%98%e6%b1%87%e6%80%bb/)
- [6. 分布式压测平台响应时间的损耗分析](https://lihuia.com/%e5%8e%8b%e6%b5%8b%e5%b9%b3%e5%8f%b0%e5%93%8d%e5%ba%94%e6%97%b6%e9%97%b4%e7%9a%84%e6%8d%9f%e8%80%97%e5%88%86%e6%9e%90/)
- [7. 平台化：JMeter脚本在线编辑初步实现](https://lihuia.com/%e5%b9%b3%e5%8f%b0%e5%8c%96%ef%bc%9ajmeter%e8%84%9a%e6%9c%ac%e5%9c%a8%e7%ba%bf%e7%bc%96%e8%be%91%e5%88%9d%e6%ad%a5%e5%ae%9e%e7%8e%b0/)
- [8. Grafana和InfluxDB帮JMeter提升性能监控的美感](https://lihuia.com/grafana%e5%92%8cinfluxdb%e5%b8%aejmeter%e5%b1%95%e7%a4%ba%e7%be%8e%e8%a7%82%e7%9a%84%e6%80%a7%e8%83%bd%e7%9b%91%e6%8e%a7/)


## 平台模块
- 后端服务：https://github.com/100ZZ/mysterious
- 前端服务：https://github.com/100ZZ/mysterious-web
- 压测引擎：https://github.com/100ZZ/mysterious-jmeter
- 其它组件：MySQL，Redis，Nginx

## 操作视频
- 安装部署：https://www.bilibili.com/video/BV1by421i7cn
- 使用说明：https://www.bilibili.com/video/BV15j421Z7mY
- 平台试用：http://101.43.119.176:1234 (demo/demo或者新注册个用户, v1.5版本)

## 测试流程
- 如果是单节点压测，只需要在一个Master节点上安装部署平台和Jmeter工具包，通过平台来调用Jmeter来执行压测用例，并返回压测结果
- 如果是分布式压测，除了Master节点安装部署平台和Jmeter工具包外，还要在Slave节点上部署Jmeter工具包，启动jmeter-server服务
![fenbu](https://github.com/user-attachments/assets/b0ed73af-f839-4485-a40e-b487da475eb0)

## 安装部署
### Docker-Compose部署方式(推荐，一键部署)
容器化部署通过docker-compose方式，如果在线拉镜像不畅，可在下面网盘里下最新v1.5版本离线容器镜像
>- 离线镜像：https://pan.baidu.com/s/128k3uiUvaKf0vgbD-BO28Q?pwd=e9qy 提取码: e9qy
<br>

1. 平台部署（默认是X86_64环境，如果是ARM环境.env替换成arm64.env启动）
>- git clone https://github.com/100ZZ/mysterious.git /root/mysterious
>- cd /root/mysterious/docker
>- ./init.sh（可以根据实际情况修改BASE_DIR，默认/opt/mysterious）
>- cd /opt/mysterious
>- git clone https://github.com/100ZZ/mysterious-jmeter.git
>- docker-compose up -d
2. 访问平台
>- 平台访问：http://xx.xx.xx.xx:1234
>- Swagger文档：http://xx.xx.xx.xx:4321/swagger-ui.html
3. 运行配置
>- 内存配置：docker-compose.yml里后端服务预分配了1G内存，可根据自身需求调整
>- Jmeter内存：mysterious-jmeter/bin/jmeter里"${HEAP:="-Xms1g -Xmx1g，自行调整
>- 配置管理：MASTER_HOST_PORT修改为本地IP:PORT，作为压测报告预览的路径前缀，修改完重启容器
4. 更新版本
>- 后端更新：更新mysterious容器(最新的docker/amd64.env覆盖.env)，重新拉镜像起容器
>- 前端更新：更新dist目录(docker/dist有最新版本目录)，覆盖/opt/mysterious/nginx/html/dist
>- 表结构变更：检查数据库脚本(docker/init.sql)，执行变更部分的sql即可

### 二进制部署方式
下面以CentOS7为例介绍下安装步骤
1. 前端部署
>- git clone https://github.com/100ZZ/mysterious-web.git
>- cd mysterious-web
>- npm install
>- npm run build (生成dist，如果不想build，mysterious的docker里有最新的dist)
2. 安装nginx，mysql，redis，jdk8+
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

3. 后端部署
>- git clone https://github.com/100ZZ/mysterious.git
>- mvn -f pom.xml clean install package -Dmaven.test.skip=true
>- mkdir -p /opt/mysterious/mysterious-data
>- mkdir -p /opt/mysterious/running
>- cp docker/mysterious.jar /opt/mysterious/running/
>- cp docker/service.sh /opt/mysterious/running/
>- cd /opt/mysterious
>- git clone https://github.com/100ZZ/mysterious-jmeter.git
>- sh /opt/mysterious/running/service.sh restart
4. 访问平台
>- 平台访问：http://xx.xx.xx.xx:1234
>- Swagger文档：http://xx.xx.xx.xx:4321/swagger-ui.html

### Slave节点部署(分布式压测，Slave节点作为压力机，启动Jmeter-Server服务)
>- 无特殊情况，推荐高配置单节点(平台管理+压力机)部署来进行压测，因为分布式压测交互也有开销
>- 无Slave节点启用，就只有Master单节点(平台管理+压力机)进行压测
>- 只要有Slave节点启用，就会作为压力机进行分布式压测，Master节点作为Client

如果需要分布式压测，找到和Master节点网络互通的Slave节点进行部署，最好是局域网，否则网络开销太大；无论Master节点是二进制还是Docker-Compose部署，Slave节点部署方式都如下
>- mkdir /opt/mysterious
>- cd /opt/mysterious
>- git clone https://github.com/100ZZ/mysterious-jmeter.git
>- 如果之前有一些测试用例，可以页面节点管理，先点击一下节点同步，会将master节点用例数据都同步到slave节点，然后启用slave节点即可
>- 只要有slave节点启用，压测都会是分布式压测，如果全都禁用，压测就只是Master单节点压测