package com.github.typesafe_query.query.internal.function;

import com.github.typesafe_query.enums.IntervalUnit;
import com.github.typesafe_query.meta.NumberDBColumn;
import com.github.typesafe_query.query.QueryContext;

public class AddFunc extends CalculationFuncBase {
	
	public AddFunc(Number expr){
		super(expr);
	}
	
	public AddFunc(NumberDBColumn<?> column){
		super(column);
	}
	
	public AddFunc(Number expr, IntervalUnit unit){
		super(expr, unit);
	}
	
	public AddFunc(NumberDBColumn<?> column, IntervalUnit unit){
		super(column, unit);
	}
	
	@Override
	public String getSQL(QueryContext context, 
			String expression) {
		super.symbol = "+";
		return super.getSQL(context, expression);
	}
}
