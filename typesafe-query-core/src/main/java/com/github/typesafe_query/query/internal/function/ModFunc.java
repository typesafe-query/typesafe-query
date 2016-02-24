package com.github.typesafe_query.query.internal.function;

import java.util.Objects;

import com.github.typesafe_query.meta.DBColumn;
import com.github.typesafe_query.query.Func;
import com.github.typesafe_query.query.QueryContext;

public class ModFunc implements Func {
	
	private Object value;
	private DBColumn<?> column;
	
	public ModFunc(Object value){
		this.value = value;
	}
	
	public ModFunc(DBColumn<?> column){
		this.column = column;
	}
	
	@Override
	public String getSQL(QueryContext context, 
			String expression) {
		if(this.value != null){
			return String.format("%s %s %s", Objects.requireNonNull(expression), "%", this.value);
		}
		else if(this.column != null){
			return String.format("%s %s %s", Objects.requireNonNull(expression), "%", context.getColumnPath(this.column));
		}
		return Objects.requireNonNull(expression);
	}
}