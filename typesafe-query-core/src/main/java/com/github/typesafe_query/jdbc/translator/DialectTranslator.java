package com.github.typesafe_query.jdbc.translator;

import java.lang.reflect.Field;
import java.sql.SQLException;

import com.github.typesafe_query.jdbc.JDBCTypeObject;
import com.github.typesafe_query.jdbc.ResultData;
import com.github.typesafe_query.jdbc.convert.TypeConverter;

public interface DialectTranslator {
	Object getValue(ResultData rd, int index, Class<?> columnJavaType, Field f) throws SQLException;
	JDBCTypeObject toJdbcTypeObject(Object value);
	JDBCTypeObject toJdbcTypeObject(Object value,TypeConverter converter);
	String toLiteral(Object o);
}
