#cd ./Shared/Civ3_Shared_Components
#mvn clean install -DskipTests
#rm -rf ./Shared/Civ3_Editor/target
cd ./Shared/Civ3_Editor
mvn clean install -DskipTests
cp -r ./src/main/java/com/civfanatics/civ3/xplatformeditor/langs ./target/langs
mvn assembly:single -DskipTests
cd target
mkdir dist
cd dist
mkdir imgs
mkdir bin
mkdir help
cp ../../imgs/*.PNG ./imgs/
cp ../../imgs/spectrum.pcx ./imgs/
cp ../../help/*.html ./help/
cp ../../bin/BIQDecompressor.jar ./bin
cp -r ../langs .
mv ../*s.jar "Conquests Editor.jar"
cp ../../../../launcher.jar .
cp ../../../../Editor_Launcher.bat .