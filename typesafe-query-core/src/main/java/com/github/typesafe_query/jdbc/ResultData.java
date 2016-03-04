package com.github.typesafe_query.jdbc;

import java.sql.ResultSetMetaData;

public interface ResultData {
	ResultSetMetaData getMetaData();
	Object get(int i);
	Object get(String name);
	
	String getString(String name);
}
