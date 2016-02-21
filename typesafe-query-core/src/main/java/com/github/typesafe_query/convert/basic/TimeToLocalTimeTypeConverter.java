package com.github.typesafe_query.convert.basic;

import java.sql.Time;
import java.time.LocalTime;

import com.github.typesafe_query.convert.TypeConverter;
import com.github.typesafe_query.query.InvalidQueryException;

public class TimeToLocalTimeTypeConverter implements TypeConverter{

	@Override
	public Object convertToDatabaseColumn(Object attribute) {
		if(attribute == null){
			return null;
		}
		
		if(!(attribute instanceof LocalTime)){
			throw new InvalidQueryException("Parameter type isnt `LocalTime`. " + attribute.getClass());
		}
		return Time.valueOf((LocalTime)attribute);
	}

	@Override
	public Object convertToEntityAttribute(Object dbData) {
		if(dbData == null){
			return null;
		}
		
		if(!(dbData instanceof Time)){
			throw new InvalidQueryException("Parameter type isnt `Time`. " + dbData.getClass());
		}

		return ((Time)dbData).toLocalTime();
	}
}
