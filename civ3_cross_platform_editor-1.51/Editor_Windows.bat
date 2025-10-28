echo %0
echo "Param One"
echo %1
echo "Percent Tilde p0"
echo %~p0
echo "Percent Tilde f0"
echo %~f0
echo "Percent Tilde fp0"
echo %~fp0
echo "Percent Tilde pf0"
echo %~pf0
echo "Percent pf0"
echo %~pf0
echo "Percent d0"
echo %~d0
pause
set dir = ""
echo "dir set"
pause
set @batLoc=%0
echo %@batLoc%
pause
REM Java location, then Jar file, then directory, then BIQ file
REM Escape the %d0%~p0 because it will end in a slash
"%~d0%~p0jre1.8_111\bin\java.exe" -jar "%~d0%~p0Conquests Editor.jar"  "%~d0%~p0\" %1
pause