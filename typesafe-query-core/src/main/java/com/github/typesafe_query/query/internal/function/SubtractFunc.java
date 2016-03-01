package com.github.typesafe_query.query.internal.function;

import com.github.typesafe_query.enums.IntervalUnit;
import com.github.typesafe_query.meta.NumberDBColumn;
import com.github.typesafe_query.query.QueryContext;

public class SubtractFunc extends CalculationFuncBase {
	
	public SubtractFunc(Number expr){
		super(expr);
	}
	
	public SubtractFunc(NumberDBColumn<?> column){
		super(column);
	}
	
	public SubtractFunc(Number expr, IntervalUnit unit){
		super(expr, unit);
	}
	
	public SubtractFunc(NumberDBColumn<?> column, IntervalUnit unit){
		super(column, unit);
	}
	
	@Override
	public String getSQL(QueryContext context, 
			String expression) {
		super.symbol = "-";
		return super.getSQL(context, expression);
	}
	
}