package com.github.typesafe_query.jdbc.convert.extra;

import com.github.typesafe_query.jdbc.convert.TypeConverter;
import com.github.typesafe_query.query.InvalidQueryException;
import com.github.typesafe_query.query.QueryException;

public class Char1ToBooleanTypeConverter implements TypeConverter{

	@Override
	public Object convertToDatabaseColumn(Object attribute) {
		if(attribute == null){
			return null;
		}
		
		if(!(attribute instanceof Boolean)){
			throw new InvalidQueryException("Parameter type isnt `Boolean`. " + attribute.getClass());
		}
		
		return ((Boolean)attribute)?"1":"0";
	}

	@Override
	public Object convertToEntityAttribute(Object dbData) {
		if(dbData == null){
			return null;
		}
		
		if(!(dbData instanceof String)){
			throw new InvalidQueryException("Parameter type isnt `String`. " + dbData.getClass());
		}
		
		if(((String)dbData).length() != 1){
			throw new QueryException("char(1) -> Boolean 変換対象ではありません。 " + dbData);
		}
		
		return "1".equals(dbData);
	}
}
