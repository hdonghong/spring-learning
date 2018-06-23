#! /bin/sh

# 首先判断程序是否已经运行，运行则停止，重启
PID=$(ps -fu `whoami` |grep 项目的jar包|grep -v grep|awk '{print $2}')
if [ -z "$PID" ];then
  echo "not running"
else
  kill -9 "$PID"
fi

nohup java -jar -Dserver.port=8080 -Dspring.profiles.active=prod demo.jar > nohup.out 2>&1 &
echo "starting..."
for i in {1..4}
do
  sleep 5
  if [ ! -f "nohuo.out" ];then
    tail -n 1 ./nohup.out
  else
    echo '不存在nohup.out'
    break
  fi
done
