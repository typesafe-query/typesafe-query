/**
 * 
 */
package com.github.typesafe_query.meta;

import java.math.BigDecimal;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public interface NumberDBColumn<T extends Number & Comparable<? super T>> extends ComparableDBColumn<T> {
	//--->conversions
	NumberDBColumn<T> max();
	NumberDBColumn<T> min();
	NumberDBColumn<BigDecimal> abs();
	NumberDBColumn<BigDecimal> avg();
	NumberDBColumn<BigDecimal> sqrt();
	NumberDBColumn<Long> count();
	NumberDBColumn<T> add(T t);
	NumberDBColumn<T> add(NumberDBColumn<?> c);
	NumberDBColumn<T> subtract(T t);
	NumberDBColumn<T> subtract(NumberDBColumn<?> c);
	NumberDBColumn<T> multiply(T t);
	NumberDBColumn<T> multiply(NumberDBColumn<?> c);
	NumberDBColumn<T> divide(T t);
	NumberDBColumn<T> divide(NumberDBColumn<?> c);
	NumberDBColumn<T> mod(T t);
	NumberDBColumn<T> mod(NumberDBColumn<?> c);
}
