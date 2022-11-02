# -*- coding: utf-8 -*-
import os
import sys
import subprocess
import time


# 根据进程名称，查询进程pid
def get_process_id(pname, uname):
	if "root" == uname:
		child = subprocess.Popen("ps -ef|grep %s|grep -v grep " % pname, stdout=subprocess.PIPE, shell=True)
		response = child.communicate()[0]
		if child.returncode == 0:
			return [int(pid) for pid in response.split()]
		return []
	else:
		child = subprocess.Popen("su - %s" % uname + " -c \"ps -ef|grep %s|grep -v grep \"" % pname, stdout=subprocess.PIPE, shell=True)
		response = child.communicate()[0]
		if child.returncode == 0:
			return [int(pid) for pid in response.split()]
		return []


# 杀掉进程
def kill_pid(pid, uname):
	if "root" == uname:
		cmd = "kill -9  %d " % pid
		os.system(cmd)
		print(cmd)
		time.sleep(2)
	else:
		cmd = "su - %s" % uname + " -c \"kill -9  %d \"" % pid
		os.system(cmd)
		print(cmd)
		time.sleep(2)


# 根据进程名杀掉进程
def kill_process_by_name(pname, uname):
	pidlst = get_process_id(pname, uname)
	if pidlst:
		for pid in pidlst:
			kill_pid(pid, uname)


def main(argv):
	# 参数解析
	print(argv)
	arg_len = len(argv)
	arg_ind = 1
	env_type = ""
	try:
		while arg_len-1 > arg_ind:
			if "-type" == argv[arg_ind] and ("1" == argv[arg_ind+1] or "2" == argv[arg_ind+1] or "3" == argv[arg_ind+1]):
				env_type = argv[arg_ind+1]
				arg_ind += 2
			else:
				print("The arguments are incorrect, support: (-type 1/2/3) !")
				sys.exit(2)
	except Exception as e:
		print(e)
		sys.exit(2)
	if arg_len == 1 or arg_ind != arg_len:
		print("The arguments are incorrect, support: (-type 1,2,3) !")
		sys.exit(2)

	try:
		if "3" == env_type:
			# Web程序
			kill_process_by_name("monitor-web.jar", "windos")
			os.system("su - windos -c \"cd /home/windos/scada-monitor/monitor-web && nohup /home/windos/scada-monitor/jdk1.8.0_211/bin/java -jar monitor-web.jar > /dev/null 2>&1 &\"")
		elif "2" == env_type:
			# Transfer程序
			kill_process_by_name("monitor-transfer.jar", "windos")
			os.system("su - windos -c \"cd /home/windos/scada-monitor/monitor-transfer && nohup /home/windos/scada-monitor/jdk1.8.0_211/bin/java -jar monitor-transfer.jar > /dev/null 2>&1 &\"")
		elif "1" == env_type:
			# Agent程序
			kill_process_by_name("monitor-agent.jar", "root")
			os.system("cd /home/windos/scada-monitor/monitor-agent && nohup /home/windos/scada-monitor/jdk1.8.0_211/bin/java -Xmx128m -Xms64m -XX:MaxMetaspaceSize=64M -jar monitor-agent.jar > /dev/null 2>&1 &")
	except Exception as e:
		print(e)
		sys.exit(2)

	sys.exit(0)


####
if __name__ == "__main__":
	main(sys.argv)
