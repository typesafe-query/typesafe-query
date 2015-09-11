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
public class GtExp<T extends Comparable<? super T>> extends ComparableExp<T> {

	/**
	 * @param left
	 * @param right
	 */
	public GtExp(IDBColumn<T> left, IDBColumn<T> right) {
		super(left, right);
	}

	/**
	 * @param left
	 * @param right
	 */
	public GtExp(IDBColumn<T> left, T right) {
		super(left, right);
	}
	
	public GtExp(IDBColumn<T> left, Param right) {
		super(left, right);
	}

	public GtExp(IDBColumn<T> left, TypesafeQuery right) {
		super(left, right);
	}

	@Override
	protected String getSQL(String l,String r) {
		return String.format("%s > %s", l,r);
	}
}
