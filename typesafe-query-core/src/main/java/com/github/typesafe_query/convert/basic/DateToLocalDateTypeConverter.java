package com.github.typesafe_query.convert.basic;

import java.sql.Date;
import java.time.LocalDate;

import com.github.typesafe_query.convert.TypeConverter;
import com.github.typesafe_query.query.InvalidQueryException;

public class DateToLocalDateTypeConverter implements TypeConverter{

	@Override
	public Object convertToDatabaseColumn(Object attribute) {
		if(attribute == null){
			return null;
		}
		
		if(!(attribute instanceof LocalDate)){
			throw new InvalidQueryException("Parameter type isnt `LocalDate`. " + attribute.getClass());
		}
		return Date.valueOf((LocalDate)attribute);
	}

	@Override
	public Object convertToEntityAttribute(Object dbData) {
		if(dbData == null){
			return null;
		}
		
		if(!(dbData instanceof Date)){
			throw new InvalidQueryException("Parameter type isnt `Date`. " + dbData.getClass());
		}

		return ((Date)dbData).toLocalDate();
	}
}
