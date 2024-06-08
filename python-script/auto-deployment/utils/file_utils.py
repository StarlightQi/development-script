import shutil
import os


def delete_directory(directory: str) -> bool:
    """
    删除指定文件目录
    :param directory: 文件目录
    :return:
    """
    try:
        shutil.rmtree(directory)
        return True
    except FileNotFoundError:
        return False
    except Exception as e:
        print(e)
        return False


def create_directory_tree(path: str) -> bool:
    """
    递归创建目录
    :param path:
    :return:
    """
    try:
        os.makedirs(path, exist_ok=True)
        return True
    except Exception as e:
        print(f"创建目录时出现异常：{e}")
        return False
