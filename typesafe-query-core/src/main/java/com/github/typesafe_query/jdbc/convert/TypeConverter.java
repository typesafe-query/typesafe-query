package com.github.typesafe_query.jdbc.convert;

public interface TypeConverter {
	Object convertToDatabaseColumn(Object attribute);
	Object convertToEntityAttribute(Object dbData);
}
