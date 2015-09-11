/**
 * 
 */
package com.github.typesafe_query.query.internal.function;

import com.github.typesafe_query.meta.IDBColumn;
import com.github.typesafe_query.query.Func;
import com.github.typesafe_query.query.QueryContext;
import com.github.typesafe_query.query.internal.QueryUtils;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class ConcatFunc implements Func {
	
	private IDBColumn<?> concatValue;
	private Object concatObject;
	
	public ConcatFunc(IDBColumn<?> concatValue) {
		this.concatValue = concatValue;
	}
	
	public ConcatFunc(Object concatObject) {
		this.concatObject = concatObject;
	}

	@Override
	public String getSQL(QueryContext context,
			String expression) {
		if(concatValue == null && concatObject == null){
			return expression;
		}
		
		if(concatValue != null){
			return "CONCAT(" + expression + "," + context.getColumnPath(concatValue) + ")";
		}
		return "CONCAT(" + expression + "," + QueryUtils.literal(concatObject) + ")";
	}
}
