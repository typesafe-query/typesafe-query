/**
 * 
 */
package com.github.typesafe_query.meta;


/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public interface INumberDBColumn<T extends Number & Comparable<? super T>> extends IComparableDBColumn<T> {
	//--->conversions
	INumberDBColumn<T> max();
	INumberDBColumn<T> min();
	INumberDBColumn<T> abs();
	INumberDBColumn<T> avg();
	INumberDBColumn<T> sqrt();
}
