# Mysterious
```
从0开始，每天撸一小段，简单易用的压测平台，提供性能相关的服务
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

![mysterious](https://user-images.githubusercontent.com/95963615/165306143-6945718e-b7b5-42d9-b104-0f50623e49bd.png)

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
java：Amazon Corretto-11.0.12.7.2
springboot：2.3.12.RELEASE
mybatis：ORM框架
druid：数据库连接池
mysql：业务数据存储
redis：排队和分布式锁
mongodb：保存结构体数据
swagger：接口文档
nginx：报告预览读取HTML目录
```
<br> 

**服务端域名**
```
本地环境：http://localhost:8888/
```
<br>

**JMeter工具包**
```
https://github.com/100ZZ/mysterious-jmeter.git
```
