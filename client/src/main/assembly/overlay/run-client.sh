#!/bin/bash

PATH_TO_CODE_BASE=`pwd`
MAIN_JAR="client/target/tpe2-g2-client-2024.Q1.jar"
java -jar "$PATH_TO_CODE_BASE/../../../../../$MAIN_JAR" "$@"
```
