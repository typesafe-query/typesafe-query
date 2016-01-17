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
}
