package com.github.typesafe_query.convert.basic;

import java.math.BigDecimal;

import com.github.typesafe_query.convert.TypeConverter;
import com.github.typesafe_query.query.InvalidQueryException;

public class BigDecimalToFloatConverter implements TypeConverter{

	@Override
	public Object convertToDatabaseColumn(Object attribute) {
		if(attribute == null){
			return null;
		}
		
		if(!(attribute instanceof Float)){
			throw new InvalidQueryException("Parameter type isnt `Float`. " + attribute.getClass());
		}

		return BigDecimal.valueOf((Float)attribute);
	}

	@Override
	public Object convertToEntityAttribute(Object dbData) {
		if(dbData == null){
			return null;
		}
		
		if(!(dbData instanceof BigDecimal)){
			throw new InvalidQueryException("Parameter type isnt `BigDecimal`. " + dbData.getClass());
		}
		return ((BigDecimal)dbData).floatValue();
	}
}
