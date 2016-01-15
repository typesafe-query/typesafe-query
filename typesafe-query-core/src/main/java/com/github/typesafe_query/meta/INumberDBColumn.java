/**
 * 
 */
package com.github.typesafe_query.meta;

import java.math.BigDecimal;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public interface INumberDBColumn<T extends Number & Comparable<? super T>> extends IComparableDBColumn<T> {
	//--->conversions
	INumberDBColumn<T> max();
	INumberDBColumn<T> min();
	INumberDBColumn<BigDecimal> abs();
	INumberDBColumn<BigDecimal> avg();
	INumberDBColumn<BigDecimal> sqrt();
}
