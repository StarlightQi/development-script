import os
import subprocess
import utils.file_utils as file


class GitTool:
    """
    Git 常用命令处理工具类
    """

    def __init__(self, directory=None, is_create=False):
        """
        初始化对象
        :param directory: 绝对路径
        """
        self.directory = directory
        if self.directory is None:
            self.directory = os.getcwd()
        # 切换目录
        os.chdir(self.directory)
        # 自动创建目录
        if is_create:
            file.create_directory_tree(directory)

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

    def run_command_all(self, commands):
        """
        运行全部命令，如果一个不成功，那边全部不成功
        param commands: 命令集合
        :return: ture 全部运行成功，false 运行失败
        """
        for command in commands:
            if self.run_command(command) is None:
                return False
        return True

    def clone_repository(self, remote):
        """
        从远程 URL 克隆 仓库到本地
        :param remote: 远程URL
        :return:
        """
        return self.run_command(f"git clone {remote}") is not None

    def check_and_pull_code(self, branch="master", clear=True) -> bool:
        """
        切换到
        :param branch: 切换分支
        :param clear: 是否清除本地改动
        :return: true 代码更新，false 代码未更新
        """
        # 重置已经修改的文件
        commands = [
            "git reset --hard",  # 重置所有已修改的文件
            "git clean -fd"  # 移除所有未跟踪的文件和目录
        ]
        if clear and not self.run_command_all(commands):
            return False

            # 拉取最新代码
        output = self.run_command(f"git pull origin ${branch}")
        if b"Already up-to-date" in output:
            return False
        else:
            return True

    def create_branch(self, new_branch):
        """
        创建并切换到新分支
        """
        return self.run_command(f"git checkout -b {new_branch}") is not None

    def commit_code(self, msg):
        """
        提交代码
        """
        return self.run_command(f"git commit -m {msg}") is not None

    def git_current_branch(self):
        """
        获取当前所在分支
        """
        branch_name = self.run_command("git rev-parse --abbrev-ref HEAD")
        if branch_name == "HEAD":
            return None
        return branch_name

    def force_commit_remote(self, branch=None):
        """
        强制同步本地分支到远程分支
        """
        if branch is None:
            branch = self.git_current_branch()
        return self.run_command(f"git push origin {branch} --force") is not None
