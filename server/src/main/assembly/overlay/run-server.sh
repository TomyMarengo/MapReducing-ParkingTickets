#!/bin/bash

PATH_TO_CODE_BASE=`pwd`
MAIN_JAR="server/target/tpe2-g2-server-2024.Q1.jar"
java -jar "$PATH_TO_CODE_BASE/../../../../../$MAIN_JAR"
