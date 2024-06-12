import os
import subprocess
import utils.file_utils as file


class Tool:
    def __init__(self, directory=None, is_create=False):
        """
        初始化对象
        :param directory: 绝对路径
        """
        self.original_path = os.getcwd()

        self.logs = []
        self.directory = directory
        if self.directory is None:
            self.directory = self.original_path
        # 切换目录
        os.chdir(self.directory)
        # 自动创建目录
        if is_create:
            file.create_directory_tree(directory)

    def check_dir(self, path):
        """
        切换工作路径
        :param path:
        :return:
        """
        os.chdir(path)

    def check_original(self):
        """
        切换回原工作路径
        :return:
        """
        os.chdir(self.original_path)

    def run_command(self, command):
        """
        运行命令
        """
        try:
            if type(command) == str:
                command = command.split(" ")
            result = subprocess.check_output(command, stderr=subprocess.STDOUT)
            return result
        except subprocess.CalledProcessError as e:
            print(f"执行命令出错了：{e.output.decode()}")

    def run_command_logs(self, command, log_add=None):
        """
        运行命令，实时返回日志
        """
        if log_add is None:
            log_add = self.log_add
        """切换到指定目录并执行命令"""
        # 创建子进程
        process = subprocess.Popen(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True,
                                   universal_newlines=True)
        # 获取输出并打印
        while True:
            output = process.stdout.readline()
            if output == "" and process.poll() is not None:
                break
            if output:
                if log_add is not None:
                    log_add(output.strip())
                else:
                    print(output.strip())
        # 返回错误信息
        return process.stderr.read()

    def validate_path(self, path):
        """
        验证路径是否存在
        """
        # if not os.path.exists(path):
        #     raise ValueError(f"Specified path does not exist:{path}")
        return path

    def log_add(self, log):
        """
        追加日志，可以父类重新实现此方法，来实现自定义日志打印
        """
        self.logs.append(log)
