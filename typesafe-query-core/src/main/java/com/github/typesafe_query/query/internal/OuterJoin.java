/**
 * 
 */
package com.github.typesafe_query.query.internal;

import com.github.typesafe_query.meta.DBTable;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class OuterJoin<T> extends InnerJoin<T> {

	public OuterJoin(DBTable joinTable,T query) {
		super(joinTable,query);
	}
}
