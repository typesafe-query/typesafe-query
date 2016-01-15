#Typesafe Query

[![GitHub version](https://badge.fury.io/gh/typesafe-query%2Ftypesafe-query.svg)](http://badge.fury.io/gh/typesafe-query%2Ftypesafe-query)
[![Build Status](https://travis-ci.org/typesafe-query/typesafe-query.svg?branch=master)](https://travis-ci.org/typesafe-query/typesafe-query)
[![Coverage Status](https://coveralls.io/repos/typesafe-query/typesafe-query/badge.svg?branch=master&service=github)](https://coveralls.io/github/typesafe-query/typesafe-query?branch=master)
[![Stories in Ready](https://badge.waffle.io/typesafe-query/typesafe-query.png?label=ready&title=Ready)](https://waffle.io/typesafe-query/typesafe-query)

##Typesafe Queryとは？
Typesafe QueryはSQLを安全に記述するためのとても便利な一連のヘルパを提供します。  
以下の特徴があります。


###柔軟なO/Rマッピング
###JDBCを(なるべく)意識させない
###ネイティブSQLを記述するようにJavaコードを記述できる
###スキーマの変更に強い
###No JPA
###Java 8 対応
Typesafe QueryではJava8から導入されたOptional、DateTime APIをO/Rマッピングに使用できます。

##インストール
Maven

```
<repositories>
  <repository>
    <id>typesafe-query</id>
    <url>http://typesafe-query.github.io/maven/</url>
  </repository>
</repositories>

```

```
<dependencies>
  <dependency>
    <groupId>com.github.typesafe-query</groupId>
    <artifactId>typesafe-query-core</artifactId>
    <version>0.0.1</version>
  </dependency>
</dependencies>
```

##使い方
###Core

[https://github.com/typesafe-query/typesafe-query/tree/master/typesafe-query-core](https://github.com/typesafe-query/typesafe-query/tree/master/typesafe-query-core)
