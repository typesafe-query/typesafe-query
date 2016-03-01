/**
 * 
 */
package com.github.typesafe_query.meta;

import com.github.typesafe_query.enums.IntervalUnit;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public interface DateDBColumn<T extends Comparable<? super T>> extends ComparableDBColumn<T> {
	DateDBColumn<T> add(Integer expr, IntervalUnit unit);
	DateDBColumn<T> add(NumberDBColumn<?> column, IntervalUnit unit);
	DateDBColumn<T> subtract(Integer expr, IntervalUnit unit);
	DateDBColumn<T> subtract(NumberDBColumn<?> column, IntervalUnit unit);
}
