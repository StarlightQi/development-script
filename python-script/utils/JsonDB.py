import json
import os
import uuid


def auto_repr(cls):
    def __repr__(self):
        data_list = []
        for k, v in self.__dict__.items():
            if k not in ["table_name", "table"]:
                data_list.append(f"{k}={v!r}")
        return f"{cls.__name__}({data_list})"

    cls.__repr__ = __repr__
    return cls


class JsonDB:
    def __init__(self, db_name, db_path=None):
        if db_path is None:
            db_path = os.getcwd()
        self.db_path = os.path.join(db_path, "db", db_name)
        if not os.path.exists(self.db_path):
            os.makedirs(self.db_path)

    def create_table(self, is_exists=True):
        pass


class BaseTable:
    excludes = ["table_path", "table_type"]

    def __init__(self, db: JsonDB, table_name, object_class):
        self.db = db
        self.table_name = table_name
        self.object_class = object_class
        self.table_path = os.path.join(db.db_path, table_name)
        if not os.path.exists(self.table_path):
            self.save(None)

    def read(self):
        pass

    def read_data(self):
        try:
            with open(self.table_path, 'r') as file:
                data = json.load(file)
                return data
        except FileNotFoundError:
            return None

    def save(self, data):
        pass

    def get_json_data(self):
        json_data = {**vars(self)}
        for exc_filed in BaseTable.excludes:
            if exc_filed in json_data:
                del json_data[exc_filed]
        return json_data

    def __str__(self):
        return json.dumps(self.get_json_data(), ensure_ascii=False)


class JsonTableList(BaseTable):
    def read(self):
        try:
            with open(self.table_path, 'r') as file:
                data: list = json.load(file)
                if data is not None:
                    return [self.object_class(**item) for item in data]
                return []
        except FileNotFoundError:
            return []

    def save(self, data):
        list_data = self.read_data()
        if list_data is None:
            list_data = []
        if data is not None:
            list_data.append(data.get_data())
        with open(self.table_path, 'w') as file:
            print(list_data)
            json.dump(list_data, file)


class JsonTableDict(BaseTable):
    def read(self) -> dict:
        try:
            with open(self.table_path, 'r') as file:
                data: dict = json.load(file)
                self.object_class(**data)
        except FileNotFoundError:
            return {}

    def save(self, data):
        dict_data = self.read_data()
        if dict_data is None:
            dict_data = {}
        if data is not None:
            dict_data = {**dict_data, **data.get_data()}
        with open(self.table_path, 'w') as file:
            print(dict_data)
            json.dump(dict_data, file)


@auto_repr
class BaseModel:
    excludes = ["table_name", "table"]

    def __init__(self, db: JsonDB, table_name, base_table: BaseTable.__class__):
        self.table_name = table_name
        self.table: base_table = base_table(db, table_name, self.__class__)

    def get_data(self):
        json_data = {**vars(self)}
        for exc_filed in BaseModel.excludes:
            if exc_filed in json_data:
                del json_data[exc_filed]
        return json_data

    def save(self):
        self.table.save(self)


class ListModel(BaseModel):
    def __init__(self, db: JsonDB, table_name, key=uuid.uuid4()):
        super().__init__(db, table_name, JsonTableList)
        self.key = key

    def get(self):
        for data in self.table.read():
            if data.get("key") == self.key:
                pass
                # 将两个对象合并为一个


class DictModel(BaseModel):
    def __init__(self, db: JsonDB, table_name):
        super().__init__(db, table_name, JsonTableDict)


"""
调用例子
"""


class TestDB(JsonDB):
    def __init__(self):
        super().__init__("test")


class TestModel(ListModel):
    def __init__(self, test="11"):
        super().__init__(TestDB(), "test1")
        self.test = test


if __name__ == '__main__':
    a = TestModel("11")
    print(a.save())
