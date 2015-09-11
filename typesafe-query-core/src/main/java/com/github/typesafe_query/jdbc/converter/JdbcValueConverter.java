package com.github.typesafe_query.jdbc.converter;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface JdbcValueConverter {
	Object getValue(ResultSet rs, int index, Class<?> propType) throws SQLException;
	Object toJdbcObject(Object value);
	String toLiteral(Object o);
}
