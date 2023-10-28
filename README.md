# Mysterious
```
分布式压测平台，提供性能相关的服务
```
<br> 

**接口文档**
```
本地环境：http://localhost:8888/swagger-ui.html
接口脚本：api/api.jmx
```
<br>

**模块功能**
<br>
[压测平台功能模块](https://di-matrix.feishu.cn/wiki/wikcn42CMTXDF89cqkkPtuKJwDe)

<br>

**项目结构**
```
doc：部署等相关文档
mysterious-common：公用类，工具
mysterious-core：DO，VO，持久层，mapper接口，xml文件
mysterious-service：业务逻辑层
mysterious-web：控制层
```
<br> 

**依赖用途**
```
java：jdk8以上
springboot：2.3.12.RELEASE
mybatis：ORM框架
druid：数据库连接池
mysql：业务数据存储
redis：排队和分布式锁
swagger：接口文档
nginx：报告预览读取HTML目录
```
<br> 

**服务端域名**
<br>

本地环境：[http://localhost:8888](http://localhost:8888)

<br>

**JMeter工具包**
<br>
[dm-jmeter.tgz](https://di-matrix.feishu.cn/file/VTLWbGNBMov047xSBixcZEeYnPd)
