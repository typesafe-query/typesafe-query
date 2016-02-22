/**
 * 
 */
package com.github.typesafe_query.meta;



/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public interface DateDBColumn<T extends Comparable<? super T>> extends ComparableDBColumn<T> {
	DateDBColumn<T> add(Integer expr, String unit);
	DateDBColumn<T> add(NumberDBColumn<?> column, String unit);
	DateDBColumn<T> subtract(Integer expr, String unit);
	DateDBColumn<T> subtract(NumberDBColumn<?> column, String unit);
}
