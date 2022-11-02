# -*- coding: utf-8 -*-
import os
import sys
import subprocess


# 设置环境变量DB_HOST
# def set_env(db_host):
# 	filepath = "/home/windos/scada-monitor/script/monitorapp_env_set_db.sh"
# 	if os.path.exists(filepath):
# 		os.system('su windos -c " sh %s ' % filepath + ' %s "' % db_host)
# 	else:
# 		print("err: no set db shell %s" % filepath)
# 		sys.exit(2)


def main(argv):
	# 参数解析
	print(argv)
	arg_len = len(argv)
	arg_ind = 1
	env_type = ""
	transfer_ip = ""
	influx_ip = ""
	db_host = ""
	# is_mysql = "" TODO 设置环境变量 uscadaIp
	try:
		while arg_len-1 > arg_ind:
			if "-type" == argv[arg_ind] and ("1" == argv[arg_ind+1] or "2" == argv[arg_ind+1] or "3" == argv[arg_ind+1]):
				env_type = argv[arg_ind+1]
				arg_ind += 2
			elif "-transfer_ip" == argv[arg_ind]:
				transfer_ip = argv[arg_ind+1]
				arg_ind += 2
			elif "-influx_ip" == argv[arg_ind]:
				influx_ip = argv[arg_ind+1]
				arg_ind += 2
			elif "-db_host" == argv[arg_ind]:
				db_host = argv[arg_ind+1]
				arg_ind += 2
			# elif "-is_mysql" == argv[arg_ind] and ("1" == argv[arg_ind+1] or "2" == argv[arg_ind+1] or "3" == argv[arg_ind+1]):
			# 	is_mysql = argv[arg_ind+1]
			# 	arg_ind += 2
			else:
				print("The arguments are incorrect, support: (-type 1/2/3) [-transfer_ip *] [-influx_ip *] [-db_host *] !")
				sys.exit(2)
	except Exception as e:
		print(e)
		sys.exit(2)
	if arg_len == 1 or arg_ind != arg_len:
		print("The arguments are incorrect, support: (-type 1/2/3) [-transfer_ip *] [-influx_ip *] [-db_host *] !")
		sys.exit(2)

	try:
		# 检查JDK
		jdk0 = subprocess.Popen("[ -d /home/windos/scada-monitor/jdk1.8.0_211 ] && exit 0 || exit 1", shell=True)
		jdk0.communicate()
		if jdk0.returncode != 0:
			jdk1 = subprocess.Popen("[ -f /home/windos/scada-monitor/jdk-8u211-linux-x64.tar.gz ] && exit 0 || exit 1", shell=True)
			jdk1.communicate()
			if jdk1.returncode == 0:
				os.system('su windos -c "cd /home/windos/scada-monitor && tar -xzf jdk-8u211-linux-x64.tar.gz"')
			else:
				print("there is no appropriate jdk, please upload it")
				sys.exit(2)
		print("JDK check finished")
		if "3" == env_type:
			# Web程序
			if "" != influx_ip:
				# 替换influxDB的IP
				os.system("sed -i '/^\\s\\+url\\:\\s\\+http\\:\\/\\/[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\:7086\\b/s/[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}/%s/g' /home/windos/scada-monitor/monitor-web/config/application.yml" % influx_ip)
			if "" != db_host:
				ret = subprocess.Popen("[ \"$(grep -c '^[[:blank:]]\\+url:[[:blank:]]\\+jdbc:mysql://' /home/windos/scada-monitor/monitor-web/config/application.yml)\" -ge 1 ] && exit 0 || exit 1", shell=True)
				ret.communicate()
				if ret.returncode == 0:
					# 如果存在，替换mysql的IP
					os.system("sed -i '/^\\s\\+url\\:\\s\\+jdbc\\:mysql\\:\\/\\/[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\:3306/s/[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}/%s/g' /home/windos/scada-monitor/monitor-web/config/application.yml" % db_host)
				else:
					# 如果不存在，新增mysql的url
					os.system("sed -i '/^\\s\\+datasource\\:$/a\\    url: jdbc:mysql://%s:3306/envision?useUnicode=true&failOverReadOnly=false&autoReconnect=true&characterEncoding=utf-8&allowMultiQueries=true' /home/windos/scada-monitor/monitor-web/config/application.yml" % db_host)
			# 设置开机自启
			ret = subprocess.Popen("[ \"$(grep -c '^su - windos -c .*monitor-web\\.jar' /etc/rc.local)\" -ge 1 ] && exit 0 || exit 1", shell=True)
			ret.communicate()
			if ret.returncode != 0:
				# 如果存在，直接跳过；如果不存在，再添加
				os.system("sed -i '$a\\su - windos -c \"cd /home/windos/scada-monitor/monitor-web && nohup /home/windos/scada-monitor/jdk1.8.0_211/bin/java -jar monitor-web.jar > /dev/null 2>&1 &\"' /etc/rc.local")
		elif "2" == env_type:
			# Transfer程序
			if "" != influx_ip:
				# 替换influxDB的IP
				os.system("sed -i '/^\\s\\+url\\:\\s\\+http\\:\\/\\/[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\:7086\\b/s/[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}/%s/g' /home/windos/scada-monitor/monitor-transfer/config/application.yml" % influx_ip)
			if "" != db_host:
				ret = subprocess.Popen("[ \"$(grep -c '^[[:blank:]]\\+url:[[:blank:]]\\+jdbc:mysql://' /home/windos/scada-monitor/monitor-transfer/config/application.yml)\" -ge 1 ] && exit 0 || exit 1", shell=True)
				ret.communicate()
				if ret.returncode == 0:
					# 如果存在，替换mysql的IP
					os.system("sed -i '/^\\s\\+url\\:\\s\\+jdbc\\:mysql\\:\\/\\/[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\:3306/s/[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}/%s/g' /home/windos/scada-monitor/monitor-transfer/config/application.yml" % db_host)
				else:
					# 如果不存在，新增mysql的url
					os.system("sed -i '/^\\s\\+datasource\\:$/a\\    url: jdbc:mysql://%s:3306/envision?useUnicode=true&failOverReadOnly=false&autoReconnect=true&characterEncoding=utf-8&allowMultiQueries=true' /home/windos/scada-monitor/monitor-transfer/config/application.yml" % db_host)
			# 设置开机自启
			ret = subprocess.Popen("[ \"$(grep -c '^su - windos -c .*monitor-transfer\\.jar' /etc/rc.local)\" -ge 1 ] && exit 0 || exit 1", shell=True)
			ret.communicate()
			if ret.returncode != 0:
				# 如果存在，直接跳过；如果不存在，再添加
				os.system("sed -i '$a\\su - windos -c \"cd /home/windos/scada-monitor/monitor-transfer && nohup /home/windos/scada-monitor/jdk1.8.0_211/bin/java -jar monitor-transfer.jar > /dev/null 2>&1 &\"' /etc/rc.local")
		elif "1" == env_type:
			# Agent程序
			if "" != transfer_ip:
				# 替换Transfer服务的IP
				os.system("sed -i '/^\\s\\+serverIp\\:\\s\\+[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\b/s/[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}/%s/g' /home/windos/scada-monitor/monitor-agent/config/application.yml" % transfer_ip)
			# 设置开机自启
			ret = subprocess.Popen("[ \"$(grep -c '^cd .*monitor-agent\\.jar' /etc/rc.local)\" -ge 1 ] && exit 0 || exit 1", shell=True)
			ret.communicate()
			if ret.returncode != 0:
				# 如果存在，直接跳过；如果不存在，再添加
				os.system("sed -i '$a\\cd /home/windos/scada-monitor/monitor-agent && nohup /home/windos/scada-monitor/jdk1.8.0_211/bin/java -Xmx128m -Xms64m -XX:MaxMetaspaceSize=64M -jar monitor-agent.jar > /dev/null 2>&1 &' /etc/rc.local")
	except Exception as e:
		print(e)
		sys.exit(2)

	sys.exit(0)


####
if __name__ == "__main__":
	main(sys.argv)
