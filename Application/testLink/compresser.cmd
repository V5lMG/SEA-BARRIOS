@echo off
:: Chemin vers le script principal
set SCRIPT_PATH=CompilateurHuffman.cmd

:: Arguments prédéfinis pour la compilation
set ACTION=compresser
set SOURCE_FILE=%1
set DESTINATION_DIR=%2
set OUTPUT_FILE=%3

:: Appeler le script principal avec des arguments
call "%SCRIPT_PATH%" %ACTION% %SOURCE_FILE% %DESTINATION_DIR% %OUTPUT_FILE%
pause
