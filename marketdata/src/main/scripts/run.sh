#!/bin/bash
#set -x

export CLASSPATH=conf
for FILE in lib/*.jar
do	
	export CLASSPATH=$CLASSPATH:$FILE
done

java -cp $CLASSPATH org.moomoocow.tammy.marketdata.Main $@