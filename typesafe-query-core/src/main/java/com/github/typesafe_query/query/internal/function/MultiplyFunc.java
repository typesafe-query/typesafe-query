package com.github.typesafe_query.query.internal.function;

import java.util.Objects;

import com.github.typesafe_query.meta.DBColumn;
import com.github.typesafe_query.query.Func;
import com.github.typesafe_query.query.QueryContext;

public class MultiplyFunc implements Func {
	
	private Number expr;
	private DBColumn<?> column;
	
	public MultiplyFunc(Number expr){
		this.expr = expr;
	}
	
	public MultiplyFunc(DBColumn<?> column){
		this.column = column;
	}
	
	@Override
	public String getSQL(QueryContext context, 
			String expression) {
		if(this.expr != null){
			return String.format("%s * %s", Objects.requireNonNull(expression), this.expr);
		}
		else if(this.column != null){
			return String.format("%s * %s", Objects.requireNonNull(expression), context.getColumnPath(this.column));
		}
		return Objects.requireNonNull(expression);
	}
}