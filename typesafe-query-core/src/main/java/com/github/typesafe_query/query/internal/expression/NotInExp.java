/**
 * 
 */
package com.github.typesafe_query.query.internal.expression;

import java.util.List;

import com.github.typesafe_query.meta.DBColumn;
import com.github.typesafe_query.query.TypesafeQuery;
import com.github.typesafe_query.query.internal.QueryUtils;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class NotInExp<T extends Comparable<? super T>> extends InExp<T> {

	public NotInExp(DBColumn<T> left, T[] objects) {
		super(left, objects);
	}

	public NotInExp(DBColumn<T> left, TypesafeQuery query) {
		super(left, query);
	}

	@Override
	protected String getSQL(String l,List<String> expressions){
		return String.format("%s NOT IN(%s)",l, QueryUtils.joinWith(",", expressions));
	}
}
