package com.github.typesafe_query.query.internal.function;

import com.github.typesafe_query.meta.NumberDBColumn;
import com.github.typesafe_query.query.QueryContext;

public class ModFunc extends CalculationFuncBase {
	
	public ModFunc(Number expr){
		super(expr); 
	}
	
	public ModFunc(NumberDBColumn<?> column){
		super(column); 
	}
	
	@Override
	public String getSQL(QueryContext context, 
			String expression) {
		super.symbol = "%";
		return super.getSQL(context, expression);
	}
	
}