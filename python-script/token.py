import json
import pyperclip
import requests
import redis

"""
pip install pyperclip
pip install redis
"""
header = {"Content-type": "application/json"}


def getRedis():
    pool = redis.ConnectionPool(host='127.0.0.1', port=6379)
    return redis.Redis(connection_pool=pool)


def myChoose():
    print("="*100)
    print("1.登录厂家端","2.登录商家端","3.自行输入url",sep="\n")
    return input("请选择1~3：")


def getUrl():
    choose = myChoose()
    if choose == "1":
        url = "http://localhost:8081/"
    elif choose == "2":
        url = "http://localhost:8082/"
    else:
        post = input("请输入端口：")
        url=f"http://localhost:{post}/"
    return url, choose


def getUser(choose):
    if choose == "1":
        str = {"username": "15578926910",
               "password": "123456"}
    elif choose == "2":
        str = {"username": "15578926910",
               "password": "123456"}
    else:
        str = {"username": input("请输入用户名称："),
               "password": input("请输入用户密码：")}
    return str


while True:
    try:
        url, choose = getUrl()
        uuid = requests.get(url + "captchaImage").json().get("uuid")
        imgCode = "captcha_codes:" + uuid
        code = str(getRedis().get(imgCode).decode(encoding="utf-8")).replace('"', "")
        data = getUser(choose)
        data["code"] = code
        data["uuid"] = uuid
        token = requests.post(url + "login", data=json.dumps(data), headers=header).json()
        if token is None:
            print("账号密码错误")
        else:
            pyperclip.copy(f"Bearer {token.get('token')}")
            pyperclip.paste()
            print(f"Bearer {token.get('token')}")
            print("运行成功，并且已经复制到剪贴板上！")
    except Exception as e:
        print(e)
        print("请求超时！")

