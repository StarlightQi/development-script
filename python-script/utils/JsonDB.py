import json
import os


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


class JsonTableList:
    excludes = ["table_path", "table_type"]

    def __init__(self, db: JsonDB, table_name, object_class):
        self.db = db
        self.table_name = table_name
        self.object_class = object_class
        self.table_path = os.path.join(db.db_path, table_name)
        if not os.path.exists(self.table_path):
            self.save(None)

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
        list_data = self.read()
        if data is not None:
            list_data.append(data.get_data())
        with open(self.table_path, 'w') as file:
            print(list_data)
            json.dump(list_data, file)

    def get_json_data(self):
        json_data = {**vars(self)}
        for exc_filed in JsonTableList.excludes:
            if exc_filed in json_data:
                del json_data[exc_filed]
        return json_data

    def __str__(self):
        return json.dumps(self.get_json_data(), ensure_ascii=False)


@auto_repr
class BaseModel:
    excludes = ["table_name", "table"]

    def __init__(self, db: JsonDB, table_name):
        self.table_name = table_name
        self.table = JsonTableList(db, table_name, self.__class__)

    def get_data(self):
        json_data = {**vars(self)}
        for exc_filed in BaseModel.excludes:
            if exc_filed in json_data:
                del json_data[exc_filed]
        return json_data


"""
调用例子
"""


class TestDB(JsonDB):
    def __init__(self):
        super().__init__("test")


class TestModel(BaseModel):
    def __init__(self, test="11"):
        super().__init__(TestDB(), "test1")
        self.test = test


if __name__ == '__main__':
    a = TestModel("11")
    print(a.table.read()[0])
