#Typesafe Query

[![Build Status](https://semaphoreci.com/api/v1/projects/b529705d-c846-4923-85fd-ded727e7c343/537605/shields_badge.svg)](https://semaphoreci.com/typesafe-query/typesafe-query)
[![Coverage Status](https://coveralls.io/repos/typesafe-query/typesafe-query/badge.svg?branch=master&service=github)](https://coveralls.io/github/typesafe-query/typesafe-query?branch=master)

##Typesafe Queryとは？
Typesafe QueryはSQLを安全に記述するためのとても便利な一連のヘルパを提供します。  


###特徴
Typesafe Queryでは以下を重要視しています。

* 柔軟なO/Rマッピング
* JDBCを(なるべく)意識させない
* ネイティブSQLを記述するようにJavaコードを記述できる
* スキーマの変更に強い

###Java8対応
Typesafe QueryではJava8から導入されたOptional、DateTime APIをO/Rマッピングに使用できます。

##インストール
Maven

```
<dependency>
  <groupId>com.github.typesafe_query</groupId>
  <artifactId>typesafe-query-core</artifactId>
  <version>0.0.2</version>
</dependency>
```

##使い方
typesafe-query-core

[https://github.com/typesafe-query/typesafe-query/tree/master/typesafe-query-core](https://github.com/typesafe-query/typesafe-query/tree/master/typesafe-query-core)
