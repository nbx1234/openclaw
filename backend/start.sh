#!/bin/bash

# 设置项目目录和日志目录
PROJECT_DIR="/home/ai02pro/my-project/backend"
LOG_DIR="$PROJECT_DIR/logs"
JAR_FILE="target/memory-system-1.0.0.jar"

# 获取当前时间，用于日志文件名
DATE=$(date +"%Y-%m-%d_%H-%M-%S")
LOG_FILE="$LOG_DIR/memory-system-$DATE.log"

# 启动 Java 程序并将输出同时显示在控制台和写入日志文件
echo "启动程序..."
nohup java -jar $PROJECT_DIR/$JAR_FILE > >(tee -a $LOG_FILE) 2>&1 &

echo "程序已启动，日志记录在 $LOG_FILE"