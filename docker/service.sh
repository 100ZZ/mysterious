#!/bin/bash

stop() {
  pid=$(ps aux | grep mysterious | grep java | awk '{print $2}')
  if [[ -n "$pid" ]]; then
    echo "开始关闭mysterious, wait ......"
    kill -9 $pid
    sleep 5
    echo "结束关闭mysterious"
  fi
}


start() {
  pid=$(ps aux | grep mysterious | grep java | awk '{print $2}')
  if [[ -n "$pid" ]]; then
    echo "mysterious is already running"
    exit 0
  else
    echo "开始启动mysterious, wait ......"
    nohup java -Xms4g -Xmx4g -Dfile.encoding=UTF-8 -jar mysterious.jar>nohup.out 2>&1 &
    sleep 5
    echo $! > run.pid
    echo "结束启动mysterious"
  fi
}

case $1 in
  'start')
    start
  ;;
  'stop')
    stop
  ;;
  'restart')
    stop
    start
  ;;
  *)
  echo "Usage: $0 {start | stop | restart}"
  exit 2
  ;;
esac
