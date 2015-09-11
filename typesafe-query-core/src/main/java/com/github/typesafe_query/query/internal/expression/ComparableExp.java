/**
 * 
 */
package com.github.typesafe_query.query.internal.expression;

import com.github.typesafe_query.meta.IDBColumn;
import com.github.typesafe_query.query.Param;
import com.github.typesafe_query.query.TypesafeQuery;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public abstract class ComparableExp<T extends Comparable<? super T>> extends BasicExp<T> {

	/**
	 * @param left
	 * @param right
	 */
	protected ComparableExp(IDBColumn<T> left, IDBColumn<T> right) {
		super(left, right);
	}

	/**
	 * @param left
	 * @param right
	 */
	protected ComparableExp(IDBColumn<T> left, T right) {
		super(left, right);
	}
	
	protected ComparableExp(IDBColumn<T> left, Param right) {
		super(left, right);
	}

	protected ComparableExp(IDBColumn<T> left, TypesafeQuery right) {
		super(left, right);
	}
}
