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
	NumberDBColumn<T> add(T expr);
	NumberDBColumn<T> add(NumberDBColumn<?> column);
	NumberDBColumn<T> subtract(T expr);
	NumberDBColumn<T> subtract(NumberDBColumn<?> column);
	NumberDBColumn<T> multiply(T expr);
	NumberDBColumn<T> multiply(NumberDBColumn<?> column);
	NumberDBColumn<T> divide(T expr);
	NumberDBColumn<T> divide(NumberDBColumn<?> column);
	NumberDBColumn<T> mod(T expr);
	NumberDBColumn<T> mod(NumberDBColumn<?> column);
}
