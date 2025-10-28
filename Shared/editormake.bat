cd *Shared*
call mvn clean install
cd ..
cd Civ3_Editor
call mvn clean
xcopy /i .\src\main\java\com\civfanatics\civ3\xplatformeditor\langs .\target\langs
call mvn install
call mvn assembly:assembly
cd *target*
mkdir dist
cd dist
xcopy ..\..\imgs\*.PNG .\imgs\
copy ..\..\imgs\spectrum.pcx .\imgs\.
xcopy ..\..\help\*.html .\help\
xcopy ..\..\..\BIQDecompressor\dist\BIQDecompressor.jar .\bin\
xcopy /i ..\langs .\langs
move ..\*s.jar "Conquests Editor.jar"
xcopy ..\..\Editor_Launcher.bat .
xcopy ..\..\launcher.jar .
7z a ConquestsEditor.zip *
explorer .
cd ../../..