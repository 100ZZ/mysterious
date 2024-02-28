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
mkdir -p mysql/data

# 创建nginx目录
mkdir -p nginx/html
mkdir -p nginx/conf.d

# 创建jmeter工具包目录
git clone https://github.com/100ZZ/mysterious-jmeter.git
rm -rf mysterious-jmeter/.git*





