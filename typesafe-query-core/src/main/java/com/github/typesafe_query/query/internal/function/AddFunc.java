package com.github.typesafe_query.query.internal.function;

import java.util.Objects;

import com.github.typesafe_query.meta.NumberDBColumn;
import com.github.typesafe_query.query.Func;
import com.github.typesafe_query.query.QueryContext;

public class AddFunc implements Func {
	
	private Number expr;
	private NumberDBColumn<?> column;
	private String unit;
	
	public AddFunc(Number expr){
		this.expr = expr;
	}
	
	public AddFunc(NumberDBColumn<?> column){
		this.column = column;
	}
	
	public AddFunc(Number expr, String unit){
		this.expr = expr;
		this.unit = unit;
	}
	
	public AddFunc(NumberDBColumn<?> column, String unit){
		this.column = column;
		this.unit = unit;
	}
	
	@Override
	public String getSQL(QueryContext context, 
			String expression) {
		if(this.unit != null){
			if(this.expr != null){
				return String.format("%s + INTERVAL %s %s", Objects.requireNonNull(expression), this.expr, this.unit);
			}
			else if(this.column != null){
				return String.format("%s + INTERVAL %s %s", Objects.requireNonNull(expression), context.getColumnPath(this.column), this.unit);
			}
			return Objects.requireNonNull(expression);
		}
		else if(this.expr != null){
			return String.format("%s + %s", Objects.requireNonNull(expression), this.expr);
		}
		else if(this.column != null){
			return String.format("%s + %s", Objects.requireNonNull(expression), context.getColumnPath(this.column));
		}
		return Objects.requireNonNull(expression);
	}
}
