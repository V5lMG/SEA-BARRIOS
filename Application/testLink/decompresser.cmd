@echo off
set SCRIPT_PATH=..\CompilateurHuffman.cmd

set ACTION=decompresser
set SOURCE_FILE=%1
set DESTINATION_DIR=%2
set OUTPUT_FILE=%3

call "%SCRIPT_PATH%" %ACTION% %SOURCE_FILE% %DESTINATION_DIR% %OUTPUT_FILE%
pause