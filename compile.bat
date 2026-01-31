@echo off
echo Compilation du projet...

REM Compiler tous les fichiers Java avec les librairies
javac -cp ".;lib/*" *.java

if %ERRORLEVEL% EQU 0 (
    echo Compilation reussie!
) else (
    echo Erreur lors de la compilation!
    pause
    exit /b 1
)

pause
