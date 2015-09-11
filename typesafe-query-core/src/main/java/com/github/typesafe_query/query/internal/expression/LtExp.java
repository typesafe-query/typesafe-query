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
public class LtExp<T extends Comparable<? super T>> extends ComparableExp<T> {

	public LtExp(IDBColumn<T> left, IDBColumn<T> right) {
		super(left, right);
	}

	public LtExp(IDBColumn<T> left, T right) {
		super(left, right);
	}
	
	public LtExp(IDBColumn<T> left, Param right) {
		super(left, right);
	}

	public LtExp(IDBColumn<T> left, TypesafeQuery right) {
		super(left, right);
	}

	@Override
	protected String getSQL(String l,String r) {
		return String.format("%s < %s", l,r);
	}
}
