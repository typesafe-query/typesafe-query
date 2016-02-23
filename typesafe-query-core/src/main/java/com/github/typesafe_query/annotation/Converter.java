package com.github.typesafe_query.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.typesafe_query.jdbc.convert.TypeConverter;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Converter {
	Class<? extends TypeConverter> value();
}
