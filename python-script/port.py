import os
import re
import time

def kill_port_process(port):
    # 根据端口号杀死进程

    ret = os.popen("netstat -nao|findstr " + str(port))
    str_list = ret.read()

    if not str_list:
        print('端口未使用')
        return
    # 只关闭处于LISTENING的端口
    if 'TCP' in str_list:
        ret_list = str_list.replace(' ', '')
        ret_list = re.split('\n', ret_list)
        listening_list = [rl.split('LISTENING') for rl in ret_list]
        process_pids = [ll[1] for ll in listening_list if len(ll) >= 2]
        process_pid_set = set(process_pids)
        for process_pid in process_pid_set:
            os.popen('taskkill /pid ' + str(process_pid) + ' /F')
            print(port, '端口已被释放')
            time.sleep(1)
        
    elif 'UDP' in str_list:
        ret_list = re.split(' ', str_list)
        process_pid = ret_list[-1].strip()
        if process_pid:
            os.popen('taskkill /pid ' + str(process_pid) + ' /F')
            print('端口已被释放')
        else:
            print("端口未被使用")

port=input("请输入您要关闭的端口：")
kill_port_process(port)
