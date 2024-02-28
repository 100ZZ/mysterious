FROM openjdk:8-jre-alpine

# 更换Alpine软件仓库源为阿里云
RUN echo "https://mirrors.aliyun.com/alpine/v3.9/main" > /etc/apk/repositories

# 安装tzdata包以设置时区
RUN apk --no-cache add tzdata

# 设置时区为Asia/Shanghai
ENV TZ=Asia/Shanghai

# 设置工作目录
WORKDIR /opt

COPY mysterious.jar /opt/

ENTRYPOINT ["java", "-jar", "/opt/mysterious.jar"]