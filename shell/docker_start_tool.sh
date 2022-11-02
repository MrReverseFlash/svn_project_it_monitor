#!/bin/bash

HOME="/home/windos"
PRJHOME="/home/windos/envision"
PIPEWORK_PATH=./pipework

funcMacAddress()
{
	net=`ls -l  /sys/class/net/ | awk '{print $9}' | grep ^e`
	declare -a arr
	local index=0
	for i in $(echo $net)
		do
    		arr[$index]=$i
    		#let "index+=1"
			((index++))
		done
	old_mac_address=`cat /sys/class/net/${arr[0]}/address`
	machex=$( echo "$old_mac_address" | tr -d ':' ) # to remove :
    macint=$( printf "%d\n" 0x$machex ) # to convert to decimal
    macint_new=$( expr $macint + $1 ) # to add node num
    mac_address=$( printf "%x\n" $macint_new ) # to convert to hex again 
	local deviation=$((12 - ${#mac_address}))
	while [ $deviation -gt 0 ];do
		mac_address=$((0))$mac_address
		((deviation--))
	done
	mac_address=$(echo ${mac_address:0:2}:${mac_address:2:2}:${mac_address:4:2}:${mac_address:6:2}:${mac_address:8:2}:${mac_address:10:2})
}

funcip2num()
{
    ip=$1
    a=$(echo $ip | awk -F'.' '{print $1}')
    b=$(echo $ip | awk -F'.' '{print $2}')
    c=$(echo $ip | awk -F'.' '{print $3}')
    d=$(echo $ip | awk -F'.' '{print $4}')
 
    #echo "$(((a << 24) + (b << 16) + (c << 8) + d))"
	ip2_num=$(((a << 24) + (b << 16) + (c << 8) + d))
}

funcGetDockerNetworkConfig()
{
	declare -i network_no=1
	local network_flag="True"
	local SECTION="network config"
	local INTERFACE_PATH="/etc/network/interfaces"
	for line in `awk "/\[$SECTION\]/{a=1}a==1" $1 |sed -e '1d' -e '/^$/d' -e '/^\[.*\]/,$d' -e '/^.*=.*/!d' | awk -F '=' '{print $1"="$2}'`
	do
		local row_no=1
		local network_flag="True"
		local card_name=`echo $line | awk -F '=' '{print $2}' | awk -F ',' '{print $1}'`
		local network_ip=`echo $line | awk -F '=' '{print $2}' | awk -F ',' '{print $2}'`
		local network_gateway=`echo $line | awk -F '=' '{print $2}' | awk -F ',' '{print $3}'`
		if [[ "$card_name" == "" ]] || [[ "$network_ip" == "" ]];then
			echo "[ERROR] row $row_no in [network config] is not valid,please check!"
			network_flag="False"
			continue
		fi
		
		if [[ $line =~ ";" ]];then
			continue
		fi
		
		if [[ -e "/etc/redhat-release" ]];then
			if [[ ! (-e "/etc/sysconfig/network-scripts/ifcfg-$card_name") ]]
			then
				echo "[ERROR] $card_name in Machine_Num $2 config doesn't exist in host environment,please check!"
				network_flag="False"
				continue
			fi
			
			local br_name=`cat /etc/sysconfig/network-scripts/ifcfg-$card_name | grep -w "BRIDGE" | awk -F '=' '{print $2}'`
			if [[ $br_name == "" ]];then
				echo "[ERROR] ifcfg-$card_name doesn't have any BRIDGE attribute,please check!"
				network_flag="False"
				continue
			elif [[ $br_name =~ "\"" ]]
			then
				br_name=`echo $br_name | tr '"' ' ' | grep -o "[^ ]\+\( \+[^ ]\+\)*"`
			fi		
			local netmask=`ifconfig $br_name | grep "inet " | awk -F " " '{print $4}'`
		elif [[ -e "/etc/os-release" ]];then
			if [[ -z `grep $card_name $INTERFACE_PATH` ]];then
				echo "$1 is not existed in linx env"
				network_flag="False"
				continue 
			fi
			
			local br_name="br$card_name"
			if [[ -z `grep $br_name $INTERFACE_PATH` ]];then
				echo "br$1 is not existed in linx env"
				network_flag="False"
				continue 
			fi
			
			local netmask=`ifconfig $br_name | grep "inet " | awk -F " " '{print $4}'`
			netmask=`echo ${netmask#*:}`
		fi
		local bytes_sum=0
		for sub_netmask in `echo $netmask | tr '.' '\n'`
		do
			bytes=`echo "obase=2;$sub_netmask" | bc | grep -o '1' |wc -l`
			bytes_sum=$((bytes+bytes_sum))
		done
		
		funcPipeWork $br_name $network_ip $bytes_sum $network_gateway 
		((network_no++))
		
	done

	if [[ $network_flag == "False" ]]
	then
		echo "Machine_Num $2 network config is not valid,docker_start_envision_muti.sh exited.."
		exit 1
	fi	
}

funcPipeWork()
{
	echo "[INFO] ready pipework network"
	if [[ -e "$PIPEWORK_PATH" ]]
	then
		chmod u+x $PIPEWORK_PATH
		funcip2num $2
		funcMacAddress $ip2_num
		if [[ "$4" == "" ]];then
			$PIPEWORK_PATH $1 -i eth$network_no $container $2/$3 $mac_address
			echo "[INFO] pipework order is $PIPEWORK_PATH $1 -i eth$network_no $pipework_hostname $2/$3 $mac_address"
		else
			$PIPEWORK_PATH $1 -i eth$network_no $container $2/$3@$4 $mac_address
			echo "[INFO] pipework order is $PIPEWORK_PATH $1 -i eth$network_no $pipework_hostname $2/$3@$4 $mac_address"
		fi
	else
		echo "[ERROR] $PIPEWORK_PATH is not exist. please check!" 
		echo "[INFO] docker_start_app.sh exited.."
		exit 1
	fi
	echo "[INFO] end pipework network"
}

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
if [ $matchingStarted ]; then
	echo "INFO:	$container is already running, now exec docker stop $matchingStarted"
	docker stop $matchingStarted
fi


matching=$(docker ps -a --filter="name=$container" -q | xargs)
if [ $matching ]; then
	echo "INFO:	$container is already exist, now exec docker rm -f $matching"
	docker rm -f $matching
fi

uname=`uname -r`
NeoKylin_rt="4.4.12-rt19.ns6"
HuNanKylin_rt="4.4.12-rt19.ky3.kb1.x86_64"
Centos_rt="4.4.12-rt19"
rt_folder="/etc/security/limits.d/99-realtime.conf"
user_name="windos"
realtime_group=`cat /etc/group | grep realtime`
if [[ $uname =~ $NeoKylin_rt ]] || [[ $uname =~ $HuNanKylin_rt ]] || [[ $uname =~ $Centos_rt ]]
then
	case $uname in
	$NeoKylin_rt)
			if [ -e "$rt_folder" ]
			then
					if [ -n "$realtime_group" ]
					then
						if [[ $realtime_group =~ $user_name  ]]
						then
							echo "INFO: Host machine is NeoKylin realtime environment"
							para=" --cpu-rt-runtime=950000 --ulimit rtprio=80 --cap-add=sys_nice --ulimit memlock=-1"
						else
							echo "ERROR: Host machine is NeoKylin realtime environment,but rt group doesn't have windos"
							para=""
						fi
					else
						echo "ERROR: Host machine is NeoKylin realtime environment,but rt group is not exists"
						para=""
					fi
			else
					echo "ERROR: Host machine is NeoKylin realtime environment,but rt folder ${rt_folder} is not exists"
					para=""
			fi
			;;
	$HuNanKylin_rt)
			if [ -e "$rt_folder" ]
			then
					if [ -n "$realtime_group" ]
					then
						if [[ $realtime_group =~ $user_name  ]]
						then
							echo "INFO: Host machine is HuNanKylin realtime environment "
							para=" --ulimit rtprio=80 --cap-add=sys_nice --ulimit memlock=-1"
						else
							echo "ERROR: Host machine is HuNanKylin realtime environment,but rt group doesn't have windos"
							para=""
						fi
					else
						echo "ERROR: Host machine is HuNanKylin realtime environment,but rt group is not exists"
						para=""
					fi
			else
					echo "ERROR: Host machine is HuNanKylin realtime environment ,but rt folder ${rt_folder} is not exists"
					para=""
			fi
			;;
	$Centos_rt)
			if [ -e "$rt_folder" ]
			then
					if [ -n "$realtime_group" ]
					then
						if [[ $realtime_group =~ $user_name  ]]
						then
							echo "INFO: Host machine is Centos realtime environment"
							para=" --ulimit rtprio=80 --cap-add=sys_nice --ulimit memlock=-1"
						else
							echo "ERROR: Host machine is Centos realtime environment,but rt group doesn't have windos"
							para=""
						fi
					else
						echo "ERROR: Host machine is Centos realtime environment,but rt group is not exists"
						para=""
					fi
			else
					echo "ERROR: Host machine is Centos realtime environment,but rt folder ${rt_folder} is not exists"
					para=""
			fi
			;;	
	esac
else
	echo "INFO: Host machine is not a realtime environment"
	para=""
fi

command="docker run --name=$container --privileged=true --network=uscada"$para

SECTION="foundation config"
ITEM="ENVISION_CONTAINER_IP"
container_ip=`awk -F '=' '/[$SECTION]/{a=1}a==1&&$1~/'$ITEM'/{print $2;exit}' $INIFILE`
if [[ ! $container_ip =~ ";" ]];then
	command=$command" --ip="$container_ip
fi

#mac address
net=`ls -l  /sys/class/net/ | awk '{print $9}' | grep ^e`
declare -a arr
index=0
for i in $(echo $net | awk '{print $1,$2}')
do
    arr[$index]=$i
    let "index+=1"
done

mac_address=`cat /sys/class/net/${arr[0]}/address`
command=$command" --mac-address="$mac_address

#tcp port mapping
SECTION="tcp port mapping"
for x in `awk "/\[$SECTION\]/{a=1}a==1" $INIFILE |sed -e '1d' -e '/^$/d' -e '/^\[.*\]/,$d' -e '/^.*=.*/!d' | awk -F '=' '{print $1":"$2}'`
do
	if [[ $x =~ ";" ]];then
		continue
	fi
	tcpport=$tcpport" -p "$x
done
command=$command$tcpport


#udp port mapping
SECTION="udp port mapping"
for x in `awk "/\[$SECTION\]/{a=1}a==1" $INIFILE |sed -e '1d' -e '/^$/d' -e '/^\[.*\]/,$d' -e '/^.*=.*/!d' | awk -F '=' '{print $1":"$2}'`
do
	if [[ $x =~ ";" ]];then
		continue
	fi
	udpport=$udpport" -p "$x"/udp"
done
command=$command$udpport


#file mapping
SECTION="file mapping"
for x in `awk "/\[$SECTION\]/{a=1}a==1" $INIFILE |sed -e '1d' -e '/^$/d' -e '/^\[.*\]/,$d' -e '/^.*=.*/!d' | awk -F '=' '{print $1":"$2}'`
do
	if [[ $x =~ ";" ]];then
		continue
	fi
	file=$file" -v "$x
done
command=$command$file


#file mapping
IFS_old=$IFS      
IFS=$'\n'            
SECTION="environment"
for x in `awk "/\[$SECTION\]/{a=1}a==1" $INIFILE |sed -e '1d' -e '/^$/d' -e '/^\[.*\]/,$d' -e '/^.*=.*/!d' | awk -F '=' '{print $1"="$2}'`
do
	if [[ $x =~ ";" ]];then
		continue
	fi
	environment=$environment" -e "$x
done
command=$command$environment
IFS=$IFS_old


command=$command" -e XMODIFIERS="@im=ibus" -e DISPLAY="$DISPLAY

windos_uid=`cat /etc/passwd | grep '^windos:' | cut -d ":" -f 3`
windos_gid=`cat /etc/passwd | grep '^windos:' | cut -d ":" -f 4`
windos_gid_group=`cat /etc/group | grep ":$windos_gid:" | cut -d ":" -f 1`
command=$command" -e USER_UID="$windos_uid
command=$command" -e USER_GID="$windos_gid:$windos_gid_group
#insert or modify .bash_profile
if grep -q  -w "USER_UID"  /home/windos/.bash_profile
then
exists=`grep -w "USER_UID" /home/windos/.bash_profile`
sed -i "s#$exists#export USER_UID=$windos_uid#g" /home/windos/.bash_profile
else
sed -i '$a export USER_UID='$windos_uid /home/windos/.bash_profile
fi

#insert or modify .bash_profile
if grep -q  -w "USER_GID"  /home/windos/.bash_profile
then
exists=`grep -w "USER_GID" /home/windos/.bash_profile`
sed -i "s#$exists#export USER_GID=$windos_gid\:$windos_gid_group#g" /home/windos/.bash_profile
else
sed -i '$a export USER_GID='$windos_gid\:$windos_gid_group /home/windos/.bash_profile
fi

SECTION="foundation config"
ITEM="HOSTNAME"
hostname=`awk -F '=' '/[$SECTION]/{a=1}a==1&&$1~/'$ITEM'/{print $2;exit}' $INIFILE`
command=$command" --hostname="$hostname

SECTION="environment"
ITEM="pasv_address"
pasv_address=`awk -F '=' '/[$SECTION]/{a=1}a==1&&$1~/'$ITEM'/{print $2;exit}' $INIFILE`

command=$command" -itd tool_base_docker /usr/sbin/init"

currTime=$(date +"%Y-%m-%d %T")
echo $currTime$command>"../log/docker_start_tool.log"
echo "INFO $currTime:	$command"

#"${command}"

rm -rf ./docker_start_temp.sh
echo "#!/bin/bash">>./docker_start_temp.sh
echo "command=\`$command\`">>./docker_start_temp.sh
chmod 755 ./docker_start_temp.sh
sh ./docker_start_temp.sh
rm -rf ./docker_start_temp.sh 

funcGetDockerNetworkConfig $INIFILE
