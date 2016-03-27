#!/bin/bash
SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do
  DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
  SOURCE="$(readlink "$SOURCE")"
  [[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE"
done
DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
MVM_HOME="/usr/local/mvm"
cd "$DIR"
echo "Building Alzheimer"
mvn package
echo "Installing Alzheimer"
cd ./target
mkdir "$MVM_HOME"
cp "./Alzheimer.jar" "$MVM_HOME"
cd "$MVM_HOME"
echo '#!/bin/bash' > './alzheimer.sh'
echo "MVM_HOME=\"$MVM_HOME\"" >> './alzheimer.sh'
echo 'MVM_Exec="$MVM_HOME"/Alzheimer.jar' >> './alzheimer.sh'
echo 'java -jar $MVM_Exec "$@"' >> './alzheimer.sh'
chmod 0755 './alzheimer.sh'
PWD=`pwd`
ln -sf "$PWD/alzheimer.sh" "/usr/local/bin/alzheimer"
