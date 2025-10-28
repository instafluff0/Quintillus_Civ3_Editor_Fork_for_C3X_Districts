export JAVA_HOME="/Library/Java/JavaVirtualMachines/liberica-jdk-21-full.jdk/Contents/Home"
export PATH="$JAVA_HOME/bin:$PATH"

cd ./Civ3_Shared_Components
mvn clean install -DskipTests
cd ../Civ3_Editor
mvn clean install -DskipTests
cp  ./src/main/java/com/civfanatics/civ3/xplatformeditor/langs ./target/langs
mvn assembly:assembly -DskipTests
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
cd ../../..