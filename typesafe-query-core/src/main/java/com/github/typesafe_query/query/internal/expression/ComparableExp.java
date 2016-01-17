/**
 * 
 */
package com.github.typesafe_query.query.internal.expression;

import com.github.typesafe_query.meta.DBColumn;
import com.github.typesafe_query.query.Param;
import com.github.typesafe_query.query.TypesafeQuery;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public abstract class ComparableExp<T extends Comparable<? super T>> extends BasicExp<T> {

	protected ComparableExp(DBColumn<T> left, DBColumn<T> right) {
		super(left, right);
	}

	protected ComparableExp(DBColumn<T> left, T right) {
		super(left, right);
	}
	
	protected ComparableExp(DBColumn<T> left, Param right) {
		super(left, right);
	}

	protected ComparableExp(DBColumn<T> left, TypesafeQuery right) {
		super(left, right);
	}
}
