#!/bin/sh
while true
do

if ! screen -list | grep -q rift; then
	echo Starting server
	sleep 5
	screen -S rift java -Xms3G -Xmx3G -XX:+UseG1GC -XX:+UnlockExperimentalVMOptions -XX:MaxGCPauseMillis=100 -XX:+DisableExplicitGC -XX:TargetSurvivorRatio=90 -XX:G1NewSizePercent=50 -XX:G1MaxNewSizePercent=80 -XX:G1MixedGCLiveThresholdPercent=35 -XX:+AlwaysPreTouch -XX:+ParallelRefProcEnabled -Dusing.aikars.flags=mcflags.emc.gs -jar paper.jar
	echo Server Screen Detatched or Ended
	sleep 5
else
	sleep 10
fi
#
#result=$(pgrep -f mcserver)
#if [ ${#result} -gt 1 ]
#then
#	sleep 10
#else
#	echo Starting server
#	sleep 5
#	screen -S mcserver java -Xms4G -Xmx4G -XX:+UseG1GC -XX:+UnlockExperimentalVMOptions -XX:MaxGCPauseMillis=100 -XX:+DisableExplicitGC -XX:TargetSurvivorRatio=90 -XX:G1NewSizePercent=50 -XX:G1MaxNewSizePercent=80 -XX:G1MixedGCLiveThresholdPercent=35 -XX:+AlwaysPreTouch -XX:+ParallelRefProcEnabled -Dusing.aikars.flags=mcflags.emc.gs -Dfile.encoding=UTF8 -jar paper.jar
#	echo Server Screen Detatched or Ended
#	sleep 5
#fi
done

