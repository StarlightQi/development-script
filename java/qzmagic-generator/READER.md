## 代码说明
主要提供了两个功能，是个模板目的是方便扩展
- 通过连接数据库获取表的详细信息
- 通过链接数据库获取表的字段信息
- 附加 通过获取的信息生成常用JavaBean

> 这个项目算是一个根据数据库生成代码模板的半成品，如果熟悉Java可以继续根据我提供的附加功能扩展生成你们公司规范的代码模板
> 
> 如果熟悉其他语言如python，JavaScript等完全可以通过http请求来获取响应数据，之后生成规范模板

## 环境配置
本系统只涉及到数据库连接，其他的内容不需要准备
需要配置环境变量（配置完成后记得重启idea）
```
MYSQL_URL         MySQL 的链接连接
MYSQL_USER        MySQL 链接用户 
MYSQL_PASSWORD    MySQL 密码 
```
或者去application.yaml 配置自己的数据库


更新日志：
2024年1月29日 根据Mapper层自动生成controller层，json请求，p1 代表第一个参数，p2代表第一个以此类推