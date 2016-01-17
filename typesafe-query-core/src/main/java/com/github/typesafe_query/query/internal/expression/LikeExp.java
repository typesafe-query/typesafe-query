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
public class LikeExp extends ComparableExp<String> {

	public LikeExp(DBColumn<String> left, String right) {
		super(left, right);
	}
	
	public LikeExp(DBColumn<String> left, Param right) {
		super(left, right);
	}

	public LikeExp(DBColumn<String> left, TypesafeQuery right) {
		super(left, right);
	}

	@Override
	protected String getSQL(String l,String r) {
		return String.format("%s LIKE %s ESCAPE '!'", l,r);
	}
}
