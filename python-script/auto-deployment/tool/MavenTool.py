import os

from Tool import Tool


class MavenTool(Tool):
    def __init__(self, directory=None, is_create=False, maven_home=None):
        """
        maven_path 安装路径
        """
        super().__init__(directory, is_create)
        if maven_home is None:
            self.maven_home = os.environ.get('MAVEN_HOME') or os.environ.get('M2_HOME')
        else:
            self.maven_home = self.validate_path(maven_home)

        if self.maven_home is None:
            self.maven_mvn_path = "mvn"
        else:
            self.maven_mvn_path = os.path.join(self.maven_home, 'bin', 'mvn')

    def run_maven_mvn(self, command):
        """
        运行Maven 命令
        """
        return self.run_command(f"{self.maven_mvn_path} {command}")

    def run_maven_mvn_log(self, command, log_add=None):
        return self.run_command_logs(f"{self.maven_mvn_path} {command}", log_add)

    def log_add(self, log):
        super().log_add(log)
        print(f"maven {log}")

    def install(self):
        return self.run_maven_mvn_log("install")
        # return self.run_maven_mvn_log("install", lambda log: print(f"test :{log}"))

    def clean(self):
        return self.run_maven_mvn_log("clear")

    def compile(self):
        return self.run_maven_mvn_log("compile")


if __name__ == '__main__':
    MavenTool(
        r"D:\work\tool-code\java\qzmagic-generator",
        maven_home=r'D:\maven3'
    ).install()
