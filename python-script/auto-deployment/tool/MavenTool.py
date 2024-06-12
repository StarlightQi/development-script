import os

from Tool import Tool
import xml.etree.ElementTree as ET


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

    def install(self, path=None):
        """
        指定开发目录
        :param path:
        :return:
        """
        if path is None or os.path.exists(path):
            return self.run_maven_mvn_log(f"install -Dmaven.repo.local={path}")
        else:
            return self.run_maven_mvn_log("install")
        # return self.run_maven_mvn_log("install", lambda log: print(f"test :{log}"))

    def get_version_from_pom(self, pom_file):
        # 解析 XML 文件
        tree = ET.parse(pom_file)
        root = tree.getroot()

        # XML 命名空间
        namespace = {'maven': 'http://maven.apache.org/POM/4.0.0'}

        # 查找 <version> 元素
        version_element = root.find('maven:version', namespace)
        if version_element is None:
            # 如果在根层次找不到 <version>，可能是在 <parent> 标签里
            parent_version = root.find('maven:parent/maven:version', namespace)
            if parent_version is not None:
                return parent_version.text
            return None
        else:
            return version_element.text

    def clean(self):
        return self.run_maven_mvn_log("clear")

    def compile(self):
        return self.run_maven_mvn_log("compile")

    def find_maven_settings(self, custom_path=None):
        # 如果提供了自定义路径，则使用该路径
        if custom_path:
            return custom_path
        # 尝试在用户的 .m2 目录下查找 settings.xml
        home_settings = os.path.expanduser('~/.m2/settings.xml')
        if os.path.exists(home_settings):
            return home_settings
        if self.maven_home:
            maven_settings = os.path.join(self.maven_home, 'conf', 'settings.xml')
            if os.path.exists(maven_settings):
                return maven_settings
        return None

    def get_local_repository_path(self, settings_path):
        settings_file_path = settings_path if settings_path else self.find_maven_settings()  # You can also pass a path here
        try:
            if settings_file_path:
                tree = ET.parse(settings_file_path)
                root = tree.getroot()
                namespace = {'maven': 'http://maven.apache.org/POM/4.0.0'}
                local_repository = root.find('maven:localRepository', namespace)
                if local_repository is not None:
                    return local_repository.text
            return None
        except Exception as e:
            return None


if __name__ == '__main__':
    MavenTool(
        r"D:\work\tool-code\java\qzmagic-generator",
        maven_home=r'D:\maven3'
    ).install()
