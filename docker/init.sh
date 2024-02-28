#!/bin/bash

BASE_DIR=/opt/mysterious

if test -d $BASE_DIR; then
  echo "$BASE_DIR is exist"
  exit
fi


mkdir $BASE_DIR

# 创建执行，日志目录
mkdir -p ${BASE_DIR}/logs

# 创建mysql目录
mkdir -p ${BASE_DIR}/mysql/sql
cp schema.sql ${BASE_DIR}/mysql/sql/
mkdir -p ${BASE_DIR}/mysql/data

# 创建nginx目录
mkdir -p ${BASE_DIR}/nginx/conf.d
cp 1234.conf ${BASE_DIR}/nginx/conf.d/
mkdir -p ${BASE_DIR}/nginx/html

# 创建压测数据，报告目录
mkdir -p ${BASE_DIR}/mysterious-data

# 创建jmeter工具包目录
cd ${BASE_DIR}
if test ! -d mysterious-jmeter; then
  git clone https://github.com/100ZZ/mysterious-jmeter.git
  rm -rf mysterious-jmeter/.git*
fi





