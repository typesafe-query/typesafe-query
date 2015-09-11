/**
 * 
 */
package com.github.typesafe_query.query.internal.function;

import com.github.typesafe_query.query.Func;
import com.github.typesafe_query.query.QueryContext;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class ToNumberFunc implements Func {
	
	private String format;
	
	public ToNumberFunc() {
	}
	
	public ToNumberFunc(String format) {
		this.format = format;
	}
	
	@Override
	public String getSQL(QueryContext context,
			String expression) {
		if(format != null){
			return String.format("TO_NUMBER(%s,%s)", expression,format);
		}
		return String.format("TO_NUMBER(%s)", expression);
	}
}
