@echo off
IF "%*" == "" GOTO Done
java -jar C:\Users\catsu\Documents\tileset-spacer\build\tileset-spacer.jar %*
:Done
pause