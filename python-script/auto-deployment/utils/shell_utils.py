import os
import subprocess


def run_command(cmd, working_directory):
    """切换到指定目录并执行命令"""
    if not os.path.exists(working_directory):
        return "指定目录不存在"
    # 切换到工作目录
    os.chdir(working_directory)
    # 创建子进程
    process = subprocess.Popen(cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True, universal_newlines=True)
    # 获取输出并打印
    while True:
        output = process.stdout.readline()
        if output == "" and process.poll() is not None:
            break
        if output:
            print(output.strip())
    # 返回错误信息
    return process.stderr.read()

