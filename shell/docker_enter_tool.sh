#!/bin/bash

INIFILE="../config/docker_start_tool.ini"
if [ ! -f "$INIFILE" ]; then
 echo "please check the file $INIFILE"
 exit 0
fi

SECTION="container name"
ITEM="CONTAINERNAME"
container_name=`awk -F '=' '/[$SECTION]/{a=1}a==1&&$1~/'$ITEM'/{print $2;exit}' $INIFILE`
container=$container_name

matchingStarted=$(docker ps --filter="name=$container" -q | xargs)
if [ ! $matchingStarted ]; then
       echo "error:  container $container is not running"
       exit
fi

command="docker exec -it"

#user_scada
windos=windos
root=root
user_in=$1
if [ ${user_in:-$windos} == $windos ] || [ ${user_in:-$windos} = $root ];then
   command=$command" --user="${user_in:-$windos}
else
   echo "error : check your user_name, user_name should be windos or root"
   exit 
fi

command=$command" -e DISPLAY="$DISPLAY

command=$command" $container /bin/bash"

if [ ${user_in:-$windos} == $windos ];then
   echo "Success : You have entered container envision as windos"
else
   echo "Success : You have entered container envision as root"
fi

$command

