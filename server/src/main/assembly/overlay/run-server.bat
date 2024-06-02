@echo off
setlocal

REM Obtiene el directorio actual
set "PATH_TO_CODE_BASE=%~dp0"

REM Especifica el JAR principal
set "MAIN_JAR=server\target\tpe2-g2-server-2024.Q1.jar"

REM Ejecuta el programa Java
java -jar "%PATH_TO_CODE_BASE%\..\..\..\..\..\%MAIN_JAR%" %*

endlocal