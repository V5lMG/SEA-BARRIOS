@echo off
:: Mettre en place le JAVA_HOME le mettre à jour le PATH
set JAVA_HOME=jdk\jdk22
set PATH=%JAVA_HOME%\bin;%PATH%

:: Liste des arguments
set action=%1
set arg1=%2
set arg2=%3
set arg3=%4

:: Exécuter l'application Java avec les arguments fournis
echo Execution de %action% avec arg1=%arg1%, arg2=%arg2%, arg3=%arg3%
java -jar jar\CompresseurHuffman.jar %action% %arg1% %arg2% %arg3%

:end
pause
