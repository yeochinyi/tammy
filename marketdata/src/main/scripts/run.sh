#!/bin/bash
#set -x

cd "$(dirname "$0")"

export CLASSPATH=conf
for FILE in lib/*.jar
do	
	export CLASSPATH=$CLASSPATH:$FILE
done

java -cp $CLASSPATH org.moomoocow.tammy.marketdata.Main $@