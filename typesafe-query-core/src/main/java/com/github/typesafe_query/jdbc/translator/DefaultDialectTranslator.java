package com.github.typesafe_query.jdbc.translator;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.typesafe_query.annotation.Converter;
import com.github.typesafe_query.jdbc.JDBCTypeObject;
import com.github.typesafe_query.jdbc.convert.TypeConverter;
import com.github.typesafe_query.jdbc.convert.basic.BigDecimalToDoubleConverter;
import com.github.typesafe_query.jdbc.convert.basic.BigDecimalToFloatConverter;
import com.github.typesafe_query.jdbc.convert.basic.DateToLocalDateTypeConverter;
import com.github.typesafe_query.jdbc.convert.basic.TimeToLocalTimeTypeConverter;
import com.github.typesafe_query.jdbc.convert.basic.TimestampToLocalDateTimeTypeConverter;
import com.github.typesafe_query.util.ClassUtils;
import com.github.typesafe_query.util.Pair;

public class DefaultDialectTranslator implements DialectTranslator{
	
	private static Logger logger = LoggerFactory.getLogger(DefaultDialectTranslator.class);
	
	private static final Map<Pair<Class<?>, Class<?>>, TypeConverter> TO_JAVATYPE_CONVERTER_MAP = new HashMap<>();
	private static final Map<Class<?>, TypeConverter> TO_DBTYPE_CONVERTER_MAP = new HashMap<>();
	
	static{
		TO_JAVATYPE_CONVERTER_MAP.put(new Pair<>(Date.class, LocalDate.class), new DateToLocalDateTypeConverter());
		TO_JAVATYPE_CONVERTER_MAP.put(new Pair<>(Time.class, LocalTime.class), new TimeToLocalTimeTypeConverter());
		TO_JAVATYPE_CONVERTER_MAP.put(new Pair<>(Timestamp.class, LocalDateTime.class), new TimestampToLocalDateTimeTypeConverter());
		TO_JAVATYPE_CONVERTER_MAP.put(new Pair<>(BigDecimal.class, Float.class), new BigDecimalToFloatConverter());
		TO_JAVATYPE_CONVERTER_MAP.put(new Pair<>(BigDecimal.class, Double.class), new BigDecimalToDoubleConverter());
		
		TO_DBTYPE_CONVERTER_MAP.put(LocalDate.class, new DateToLocalDateTypeConverter());
		TO_DBTYPE_CONVERTER_MAP.put(LocalTime.class, new TimeToLocalTimeTypeConverter());
		TO_DBTYPE_CONVERTER_MAP.put(LocalDateTime.class, new TimestampToLocalDateTimeTypeConverter());
	}

	@Override
	public Object getValue(ResultSet rs, int index, Class<?> columnJavaType, Field f) throws SQLException {
		Class<?> propType = f.getType();
		//==== Moved from BeanResultMapper issues#6
		//Optional
		if(Optional.class.isAssignableFrom(f.getType())){
			ParameterizedType pt = (ParameterizedType)f.getGenericType();
			Type[] types = pt.getActualTypeArguments();
			propType = (Class<?>)types[0];
			logger.debug("Optional<{}>",propType);
		}
		
		Object o = getValue(rs, index, columnJavaType, propType, f);
		
		//==== Moved from BeanResultMapper issues#6
		//Optional
		if(Optional.class.isAssignableFrom(f.getType())){
			o = Optional.ofNullable(o);
		}
		return o;
	}
	
	private TypeConverter getTypeConverter(Class<?> from, Class<?> to, Field f){
		if(f.isAnnotationPresent(Converter.class)){
			Converter c = f.getAnnotation(Converter.class);
			Class<? extends TypeConverter> converter = c.value();
			return ClassUtils.newInstance(Objects.requireNonNull(converter));
		}
		return getDefaultTypeConverter(from, to);
	}
	
	protected TypeConverter getDefaultTypeConverter(Class<?> from, Class<?> to){
		Optional<Pair<Class<?>,Class<?>>> o = TO_JAVATYPE_CONVERTER_MAP.keySet()
			.stream()
			.filter(p -> p._1.equals(from) && p._2.equals(to))
			.findFirst();
		
		if(o.isPresent()){
			return TO_JAVATYPE_CONVERTER_MAP.get(o.get());
		}
		return null;
	}
	
	protected TypeConverter getDefaultTypeConverter(Class<?> to){
		return TO_DBTYPE_CONVERTER_MAP.get(to);
	}

	protected Object getValue(ResultSet rs, int index, Class<?> columnJavaType, Class<?> propType, Field f) throws SQLException {
		if (!propType.isPrimitive() && rs.getObject(index) == null ) {
			return null;
		}
		
		//ResuleSetMetadata#getColumnClassNameに変換する
		Object result = rs.getObject(index);
		propType = ClassUtils.primitiveToWrapperClass(propType);

		//propTypeとクラスが同じならリターン
		logger.trace("proptype={} resultType={}",propType,result.getClass());
		if(propType.isAssignableFrom(result.getClass())){
			return result;
		}
		
		//Converterを探す (From=ColumnClass To=PropTypeClass)
		TypeConverter converter = getTypeConverter(result.getClass(), propType, f);
		if(converter != null){
			return converter.convertToEntityAttribute(result.getClass().cast(result));
		}
		throw new RuntimeException("Typesafe Queryで対応していない型です " + propType.getName() + " expect=" + result.getClass());
	}

	@Override
	public JDBCTypeObject toJdbcTypeObject(Object value) {
		return toJdbcTypeObject(value, null);
	}

	@Override
	public JDBCTypeObject toJdbcTypeObject(Object value, TypeConverter converter) {
		//Optional判定
		if(value instanceof Optional){
			Optional<?> op = Optional.class.cast(value);
			if(op == null){
				throw new RuntimeException("Optional getXxx() called but return value is null. Optional must return NOT null value!!");
			}
			value = op.orElse(null);
		}
		
		if(value == null){
			return new JDBCTypeObject(null);
		}
		
		//対応していない型はエラーにする
		//FIXME やさしさで以下のコードは存在したが、復活させたい！！これがないとSQL実行時にObject#toStringしたものがパラメーターにセットされたりする。
//		Class<?> targetClass = value.getClass();
//		if(JDBC_CLASSES.stream().noneMatch(c -> c.isAssignableFrom(targetClass))){
//			throw new InvalidQueryException("Type " + targetClass.getName() + " is not supported by JDBC.");
//		}
		
		if(converter == null){
			converter = getDefaultTypeConverter(value.getClass());
		}
		
		if(converter != null){
			return new JDBCTypeObject(converter.convertToDatabaseColumn(value));
		}
		return new JDBCTypeObject(value);
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
