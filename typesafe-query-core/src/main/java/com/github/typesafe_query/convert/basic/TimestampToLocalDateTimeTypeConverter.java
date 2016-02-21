package com.github.typesafe_query.convert.basic;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.github.typesafe_query.convert.TypeConverter;
import com.github.typesafe_query.query.InvalidQueryException;

public class TimestampToLocalDateTimeTypeConverter implements TypeConverter{

	@Override
	public Object convertToDatabaseColumn(Object attribute) {
		if(attribute == null){
			return null;
		}
		
		if(!(attribute instanceof LocalDateTime)){
			throw new InvalidQueryException("Parameter type isnt `LocalDateTime`. " + attribute.getClass());
		}
		return Timestamp.valueOf((LocalDateTime)attribute);
	}

	@Override
	public Object convertToEntityAttribute(Object dbData) {
		if(dbData == null){
			return null;
		}
		
		if(!(dbData instanceof Timestamp)){
			throw new InvalidQueryException("Parameter type isnt `Timestamp`. " + dbData.getClass());
		}

		return ((Timestamp)dbData).toLocalDateTime();
	}
}
