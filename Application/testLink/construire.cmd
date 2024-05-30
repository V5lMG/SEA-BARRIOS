@echo off
set SCRIPT_PATH=..\CompilateurHuffman.cmd

set ACTION=construire
set BASE_FILE=%1
set DESTINATION_DIR=%2

call "%SCRIPT_PATH%" %ACTION% %BASE_FILE% %DESTINATION_DIR%
pause
