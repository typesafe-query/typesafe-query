package com.github.typesafe_query.jdbc.convert.basic;

import java.math.BigDecimal;

import com.github.typesafe_query.jdbc.convert.TypeConverter;
import com.github.typesafe_query.query.InvalidQueryException;

public class BigDecimalToDoubleConverter implements TypeConverter{

	@Override
	public Object convertToDatabaseColumn(Object attribute) {
		if(attribute == null){
			return null;
		}
		
		if(!(attribute instanceof Double)){
			throw new InvalidQueryException("Parameter type isnt `Double`. " + attribute.getClass());
		}

		return BigDecimal.valueOf((Double)attribute);
	}

	@Override
	public Object convertToEntityAttribute(Object dbData) {
		if(dbData == null){
			return null;
		}
		
		if(!(dbData instanceof BigDecimal)){
			throw new InvalidQueryException("Parameter type isnt `BigDecimal`. " + dbData.getClass());
		}
		return ((BigDecimal)dbData).doubleValue();
	}
}
