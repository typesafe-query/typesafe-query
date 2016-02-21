package com.github.typesafe_query.convert;

public interface TypeConverter {
	Object convertToDatabaseColumn(Object attribute);
	Object convertToEntityAttribute(Object dbData);
}
