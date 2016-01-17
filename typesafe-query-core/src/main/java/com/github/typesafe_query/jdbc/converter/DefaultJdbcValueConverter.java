package com.github.typesafe_query.jdbc.converter;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.github.typesafe_query.query.InvalidQueryException;
import com.github.typesafe_query.util.DateTimeUtils;

public class DefaultJdbcValueConverter implements JdbcValueConverter{
	
	private static final Set<Class<?>> JDBC_CLASSES = new HashSet<>();
	
	static{
		//TODO v0.3.x CLOB BLOB XMLなど #6
		JDBC_CLASSES.add(String.class);
		JDBC_CLASSES.add(Short.class);
		JDBC_CLASSES.add(Integer.class);
		JDBC_CLASSES.add(Long.class);
		JDBC_CLASSES.add(Float.class);
		JDBC_CLASSES.add(Double.class);
		JDBC_CLASSES.add(BigDecimal.class);
		JDBC_CLASSES.add(Boolean.class);
		
		JDBC_CLASSES.add(LocalDate.class);
		JDBC_CLASSES.add(LocalTime.class);
		JDBC_CLASSES.add(LocalDateTime.class);
		
		JDBC_CLASSES.add(Date.class);
		JDBC_CLASSES.add(Time.class);
		JDBC_CLASSES.add(Timestamp.class);
	}

	@Override
	public Object getValue(ResultSet rs, int index, Class<?> propType) throws SQLException {
		
		if (!propType.isPrimitive() && rs.getObject(index) == null ) {
			return null;
		}

		if (propType.equals(String.class)) {
			return rs.getString(index);
		} else if (
				propType.equals(Boolean.TYPE) || propType.equals(Boolean.class)) {
				return Boolean.valueOf(rs.getBoolean(index));
		} else if (propType.equals(Integer.TYPE) || propType.equals(Integer.class)) {
			Object o = rs.getObject(index);
			if(o instanceof String){
				o = new BigDecimal((String)o);
			}
			return Integer.valueOf(((Number)o).intValue());
		} else if (propType.equals(Long.TYPE) || propType.equals(Long.class)) {
			Object o = rs.getObject(index);
			if(o instanceof String){
				o = new BigDecimal((String)o);
			}
			return Long.valueOf(((Number)o).longValue());
		} else if (
			propType.equals(Double.TYPE) || propType.equals(Double.class)) {
			Object o = rs.getObject(index);
			if(o instanceof String){
				o = new BigDecimal((String)o);
			}
			return Double.valueOf(((Number)o).doubleValue());
		} else if (
			propType.equals(Float.TYPE) || propType.equals(Float.class)) {
			Object o = rs.getObject(index);
			if(o instanceof String){
				o = new BigDecimal((String)o);
			}
			return Float.valueOf(((Number)o).floatValue());
		} else if (
			propType.equals(Short.TYPE) || propType.equals(Short.class)) {
			Object o = rs.getObject(index);
			if(o instanceof String){
				o = new BigDecimal((String)o);
			}
			return Short.valueOf(((Number)o).shortValue());
//		} else if (propType.equals(Byte.TYPE) || propType.equals(Byte.class)) {
//			return Byte.valueOf(((Number)rs.getObject(index)).byteValue());
		} else if (propType.equals(LocalDate.class)) {
			Date d = rs.getDate(index);
			return DateTimeUtils.toLocalDate(d);
		} else if (propType.equals(LocalTime.class)) {
			Time d = rs.getTime(index);
			return DateTimeUtils.toLocalTime(d);
		} else if (propType.equals(LocalDateTime.class)) {
			Timestamp d = rs.getTimestamp(index);
			return DateTimeUtils.toLocalDateTime(d);
		} else if (propType.equals(Date.class)) {
			return rs.getDate(index);
		} else if (propType.equals(Time.class)) {
			return rs.getTime(index);
		} else if (propType.equals(Timestamp.class)) {
			return rs.getTimestamp(index);
		} else if (propType.equals(SQLXML.class)) {
			return rs.getSQLXML(index);
		} else if(propType.equals(BigDecimal.class)){
			return rs.getBigDecimal(index);
		} 
		throw new RuntimeException("Typesafe Queryで対応していない型です " + propType.getName());
	}

	@Override
	public Object toJdbcObject(Object value) {
		//Optional判定
		if(value instanceof Optional){
			Optional<?> op = Optional.class.cast(value);
			if(op == null){
				throw new RuntimeException("Optional getXxx() called but return value is null. Optional must return NOT null value!!");
			}
			value = op.orElse(null);
		}
		
		if(value == null){
			return null;
		}
		
		Class<?> targetClass = value.getClass();
		//対応していない型はエラーにする
		if(!JDBC_CLASSES.stream().anyMatch(c -> c.isAssignableFrom(targetClass))){
			throw new InvalidQueryException("Type " + targetClass.getName() + " is not supported by JDBC.");
		}

		if(value instanceof LocalDate){
			LocalDate d = LocalDate.class.cast(value);
			value = DateTimeUtils.toDate(d);
		}else if(value instanceof LocalTime){
			LocalTime d = LocalTime.class.cast(value);
			value = DateTimeUtils.toTime(d);
		}else if(value instanceof LocalDateTime){
			LocalDateTime d = LocalDateTime.class.cast(value);
			value = DateTimeUtils.toTimestamp(d);
		}
		return value;
	}

	@Override
	public String toLiteral(Object o) {
		if(o == null){
			return null;
		}
		
		if(o instanceof LocalDate){
			LocalDate d = LocalDate.class.cast(o);
			String literal = d.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			return String.format("'%s'", literal);
		}else if(o instanceof LocalTime){
			LocalTime d = LocalTime.class.cast(o);
			String literal = d.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
			return String.format("'%s'", literal);
		}else if(o instanceof LocalDateTime){
			LocalDateTime d = LocalDateTime.class.cast(o);
			String literal = d.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
			return String.format("'%s'", literal);
		}else if(o instanceof String
				|| o instanceof java.sql.Date
				|| o instanceof java.sql.Timestamp
				|| o instanceof java.sql.Time){
			return String.format("'%s'", o.toString().replaceAll("'", "''"));
		}
		return o.toString();
	}
}
