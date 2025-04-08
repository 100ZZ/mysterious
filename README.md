# Mysterious
分布式压测平台，前端Vue3+TypeScript，后端SpringBoot-2.3.12，压测引擎JMeter-5.5，支持JMeter的分布式压测，管理，执行，报告，日志等。因为设计，开发，测试都是自己，一些功能优化需求也是根据自己的使用来改进，目前主要优先考虑正确性和实用性，未来通过大模型生成JMX脚本也在考虑当中。

https://github.com/user-attachments/assets/514c7ab6-08a6-4576-9af1-9dafe60a0589

## 🔥 经验总结
- [1. JMeter分布式压测](https://lihuia.com/jmeter%e5%88%86%e5%b8%83%e5%bc%8f%e5%8e%8b%e6%b5%8b/)
- [2. JMeter有关JAR依赖异常问题](https://lihuia.com/jmeter%e6%9c%89%e5%85%b3jar%e4%be%9d%e8%b5%96%e7%9a%84%e9%97%ae%e9%a2%98/)
- [3. 吐血定位端口映射影响JMeter分布式压测的异常问题](https://lihuia.com/%e5%90%90%e8%a1%80%e5%ae%9a%e4%bd%8d%e7%ab%af%e5%8f%a3%e6%98%a0%e5%b0%84%e5%bd%b1%e5%93%8djmeter%e5%88%86%e5%b8%83%e5%bc%8f%e5%8e%8b%e6%b5%8b%e7%9a%84%e5%bc%82%e5%b8%b8%e9%97%ae%e9%a2%98/)
- [4. JMeter分布式压测启动流程简述](https://lihuia.com/jmeter%e5%88%86%e5%b8%83%e5%bc%8f%e5%8e%8b%e6%b5%8b%e5%90%af%e5%8a%a8%e6%b5%81%e7%a8%8b%e7%ae%80%e8%bf%b0/)
- [5. JMeter分布式平台化相关的异常问题汇总](https://lihuia.com/jmeter%e5%88%86%e5%b8%83%e5%bc%8f%e7%9b%b8%e5%85%b3%e7%9a%84%e5%bc%82%e5%b8%b8%e9%97%ae%e9%a2%98%e6%b1%87%e6%80%bb/)
- [6. 分布式压测平台响应时间的损耗分析](https://lihuia.com/%e5%8e%8b%e6%b5%8b%e5%b9%b3%e5%8f%b0%e5%93%8d%e5%ba%94%e6%97%b6%e9%97%b4%e7%9a%84%e6%8d%9f%e8%80%97%e5%88%86%e6%9e%90/)
- [7. 平台化：JMeter脚本在线编辑初步实现](https://lihuia.com/%e5%b9%b3%e5%8f%b0%e5%8c%96%ef%bc%9ajmeter%e8%84%9a%e6%9c%ac%e5%9c%a8%e7%ba%bf%e7%bc%96%e8%be%91%e5%88%9d%e6%ad%a5%e5%ae%9e%e7%8e%b0/)
- [8. Grafana和InfluxDB帮JMeter提升性能监控的美感](https://lihuia.com/grafana%e5%92%8cinfluxdb%e5%b8%aejmeter%e5%b1%95%e7%a4%ba%e7%be%8e%e8%a7%82%e7%9a%84%e6%80%a7%e8%83%bd%e7%9b%91%e6%8e%a7/)


## 🔥 平台模块
- 后端服务：https://github.com/100ZZ/mysterious
- 前端服务：https://github.com/100ZZ/mysterious-web
- 压测引擎：https://github.com/100ZZ/mysterious-jmeter
- 平台试用：http://120.55.52.10:1234 (demo/demo或者新注册个用户)

## 🔥 测试说明
- 操作说明：基于JMeter的测试逻辑，因此正常就直接按照JMeter的使用流程来进行操作
- 问题定位：包括平台的异常报错和JMeter本身的压测异常；平台的异常直接结合源代码看服务日志即可，JMeter的报错就需要自身的定位能力，甚至压测有时候执行是成功的，但却是异常的（比如BeanShell里缺少个依赖），所以和直接使用JMeter一样，需要自身的一些敏感度和对结果的判断性，在这里我已经将可能出现的报错类型通过可视化的方式呈现出来了，方便定位
  ```
  - 日志：展示jmeter.log的所有信息，因此任何报错都可以直接通过这里查看原因
  - 预览：展示压测最终的聚合结果，原生jmeter的报告，里面也会有一些错误信息
  - 下载：如果需要整个压测报告，可以直接下载整个静态HTML文件夹，本地直接打开
  ```

## 🔥 测试流程
- 本地上传：本地能运行的测试，上传测试的JMX，CSV，JAR等依赖，平台均可直接处理和执行，日志，报告
  ```
  - 新建用例：会在Master节点创建用例命名空间路径
  - 上传JMX：本地调试好的JMX（比如已经关联了本地的CSV和JAR），页面上传操作，会存放到对应用例路径下
  - 上传CSV：有一个限制，本地调试的时候，CSV Data Set Config的Name需要设置为上传的CSV文件名带后缀，以便上传后平台修改CSV模块
  - 上传JAR：没啥限制，上传后，平台会修改JMX脚本的ClassPath模块
  ```
- 在线生成：主要是JMX脚本，完成一些简单的脚本进行在线编写，毕竟JMeter各种插件众多，不可能都兼容
  ```
  - Threads：ThreadGroup，SteppingThreadGroup，ConCurrencyThreadGroup
  - Sampler：HttpSampler，JavaRequest，DubboSampler
  - Assertion：ResponseCode，ResponseMessage，JSONPath
  - 其它：如果还有无法满足的测试需求，可以直接本地上传的方式来进行测试
  ```
- 智能生成：后续考虑结合AI大模型，提供可视化prompt提示词输入，按要求生成JMX脚本和CSV文件

## 🔥 部署架构
- 如果是单节点压测，只需要在Master节点上安装部署平台（包括压测引擎），通过平台来调用本地压测引擎进行单节点压测，返回压测结果
- 如果是分布式压测，除了Master节点安装部署平台（包括压测引擎）外，还要在Slave节点上部署压测引擎，启动jmeter-server服务
<kbd>![fenbu](https://github.com/user-attachments/assets/b0ed73af-f839-4485-a40e-b487da475eb0)</kbd>

## 🔥 安装部署
### ⚙️ Master节点：平台Docker-Compose部署方式（推荐，一键部署）
容器化部署通过docker-compose方式在Master节点一键部署压测平台，如果在线拉镜像不畅，网盘里下最新版本离线容器镜像（包括x86和arm64）
> 离线镜像：https://pan.baidu.com/s/128k3uiUvaKf0vgbD-BO28Q?pwd=e9qy 提取码: e9qy

1. 平台部署
>- git clone https://github.com/100ZZ/mysterious.git
>- cd mysterious/docker
>- sh init.sh amd64|arm64 （参数：x86传amd64，arm传arm64；init.sh可自定义平台数据目录）
>- cd ./mysterious
>- git clone https://github.com/100ZZ/mysterious-jmeter.git
>- docker-compose up -d
2. 访问平台
>- 平台访问：http://xx.xx.xx.xx:1234
>- Swagger文档：http://xx.xx.xx.xx:4321/swagger-ui.html
3. 运行配置
>- 内存配置：docker-compose.yml里后端服务预分配了1G内存，可根据需求调整
>- 引擎内存：mysterious-jmeter/bin/jmeter里配置HEAP内存2g-4g-512m，可根据需求自行调整
>- 配置管理：MASTER_HOST_PORT修改为本地IP:PORT，作为压测报告预览的路径前缀，修改完重启容器
>- 运行路径：Master和Slave节点有关引擎的执行路径都可以在配置管理里进行修改，然后重启容器
4. 版本更新
>- 后端更新：更新mysterious容器(最新的docker/${架构}.env配置覆盖${BASE_DIR}/.env)，重启容器
>- 前端更新：更新dist目录，覆盖docker/mysterious/nginx/html/dist，重启mysterious-nginx容器
>- 库表变更：检查数据库脚本(docker/init.sql)，执行变更部分的sql即可；因为db容器初始化sql只首次生效

### ⚙️ Master节点：平台二进制部署方式
下面以CentOS7为例介绍下安装步骤
1. 前端部署
>- git clone https://github.com/100ZZ/mysterious-web.git
>- cd mysterious-web
>- npm install
>- npm run build (生成dist，如果不想build，mysterious的docker里有最新的dist)
2. 安装nginx，mysql，redis，jdk8+
> nginx
>- 1234.conf和9998.conf复制到/etc/nginx/conf.d下（根据系统实际情况），并修改下，比如mysterious-nginx改成localhost，mysterious改成最后前端页面访问的IP地址，一些location路径一并修改
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
>- 同样的内存配置相关的，参考docker-compose部分的说明

### ⚙️ Slave节点：压测引擎部署（分布式压测，Slave作为压力机，启动Jmeter-Server服务）
>- 重要：无特殊情况，推荐高配置单节点部署来进行压测就行了，因为分布式压测交互也有开销
>- 无Slave节点Agent服务启用，就只有Master单节点进行压测
>- 只要有Slave节点Agent服务启用，就会进行分布式压测，Master节点作为Client

如果需要分布式压测，找到和Master节点网络互通的Slave节点进行部署，最好是局域网，否则网络开销太大；无论Master节点是二进制还是Docker-Compose部署，Slave节点部署方式都如下
>- mkdir /opt/mysterious
>- cd /opt/mysterious
>- git clone https://github.com/100ZZ/mysterious-jmeter.git
>- 如果之前有一些测试用例，可以页面节点管理，先点击一下节点同步，会将Master节点用例数据都同步到Slave节点，然后启用Slave节点即可
>- 只要有Slave节点启用，压测都会是分布式压测，如果全都禁用，压测就只是Master单节点压测
>- 具体Slave节点的目录结构都是可配置的，具体可以参考配置管理页面
>- 同样的内存配置相关的，参考docker-compose部分的说明
