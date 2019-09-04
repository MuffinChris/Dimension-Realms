#!/bin/sh
while true
do
result=$(ps -A|grep java)
if [ ${#result} -gt 0 ]
then
	sleep 10
else
	echo Starting server
	sleep 5
	screen -S mcserver java -Dfile.encoding=UTF8 -Xms512m -Xmx6G -jar paper-175.jar
	echo Server Screen Detatched or Ended
	sleep 5
fi
done

