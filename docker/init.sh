#!/bin/bash

BASE_DIR=/opt/mysterious

if test -d $BASE_DIR; then
  echo "$BASE_DIR is exist"
  exit
fi


mkdir $BASE_DIR
cd $BASE_DIR

# 创建mysql目录
mkdir -p mysql/sql
cp schema.sql ${BASE_DIR}/mysql/sql/
cp insert.sql ${BASE_DIR}/mysql/sql/
mkdir -p mysql/data

# 创建nginx目录
mkdir -p nginx/conf.d
cp 1234.conf ${BASE_DIR}/nginx/conf.d/
mkdir -p nginx/html

# 创建压测数据，报告目录
mkdir -p mysterious-data

# 创建jmeter工具包目录
git clone https://github.com/100ZZ/mysterious-jmeter.git
rm -rf mysterious-jmeter/.git*





