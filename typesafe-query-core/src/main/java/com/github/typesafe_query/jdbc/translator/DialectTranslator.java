package com.github.typesafe_query.jdbc.translator;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.github.typesafe_query.convert.TypeConverter;
import com.github.typesafe_query.jdbc.JDBCTypeObject;

public interface DialectTranslator {
	Object getValue(ResultSet rs, int index, Class<?> columnJavaType, Field f) throws SQLException;
	JDBCTypeObject toJdbcTypeObject(Object value);
	JDBCTypeObject toJdbcTypeObject(Object value,TypeConverter converter);
	String toLiteral(Object o);
}
