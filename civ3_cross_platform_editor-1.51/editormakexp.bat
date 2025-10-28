cd *Shared*
call mvn clean install
cd ..
cd Civ3_Editor
call mvn clean
REM /i assumes non-existant destination is a directory (instead of interactively asking user)
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
REM /e recursively copies subfolders
xcopy /i /e ..\..\..\jre1.8.0_111_x86 .\jre1.8_111
move ..\*s.jar "Conquests Editor.jar"
xcopy ..\..\Editor_XP.bat .
xcopy ..\..\XP_launcher.vbs .
7z a ConquestsEditorXP.7z *
explorer .
cd ..\..\..
