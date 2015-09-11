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
public class LikeExp extends ComparableExp<String> {
	/**
	 * @param left
	 * @param right
	 */
	public LikeExp(IDBColumn<String> left, String right) {
		super(left, right);
	}
	
	public LikeExp(IDBColumn<String> left, Param right) {
		super(left, right);
	}

	public LikeExp(IDBColumn<String> left, TypesafeQuery right) {
		super(left, right);
	}

	@Override
	protected String getSQL(String l,String r) {
		return String.format("%s LIKE %s ESCAPE '!'", l,r);
	}
}
