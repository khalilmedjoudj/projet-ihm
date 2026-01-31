@echo off
echo Compilation et lancement de l'application...
echo.

REM Compiler tous les fichiers Java avec les librairies
echo [1/2] Compilation...
javac -cp ".;lib/*" *.java

if %ERRORLEVEL% NEQ 0 (
    echo Erreur lors de la compilation!
    pause
    exit /b 1
)

echo Compilation reussie!
echo.

REM Executer l'application avec les librairies
echo [2/2] Lancement de l'application...
java -cp ".;lib/*" Main

pause
