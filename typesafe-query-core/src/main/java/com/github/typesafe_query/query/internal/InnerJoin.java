/**
 * 
 */
package com.github.typesafe_query.query.internal;


import com.github.typesafe_query.meta.DBTable;
import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.Join;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class InnerJoin<T> implements Join<T> {
	
	private DBTable joinTable;
	
	private T query;
	
	private Exp on;
	
	public InnerJoin(DBTable joinTable,T query) {
		this.joinTable = joinTable;
		this.query = query;
	}

	@Override
	public T on(Exp exp) {
		this.on = exp;
		return query;
	}

	@Override
	public DBTable getTargetTable() {
		return joinTable;
	}

	@Override
	public Exp getOn() {
		return on;
	}
}
