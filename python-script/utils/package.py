import os
import sys

if sys.platform == "win32":
    os.system("chcp 65001")  # 设置命令行编码为 UTF-8

# 读取 requirements.txt 文件中的库
with open('requirements.txt', 'r', encoding="gbk") as f:
    libs = f.read().splitlines()

# 将库名加入到 PyInstaller 打包命令中的 --hidden-import 参数中
hidden_imports = ''
for lib in libs:
    hidden_imports += f' --hidden-import={lib.split("==")[0]}'

command = f'pyinstaller -i icon.ico --onefile --windowed {hidden_imports}  --add-data ".\\venv10\\Lib\\site-packages\\zhconv;zhconv" main.py'
# command = f'pyinstaller -i icon.ico  {hidden_imports}  --add-data ".\\venv\\Lib\\site-packages\\zhconv;zhconv"  main.py'
print(command)
# os.system(command)
