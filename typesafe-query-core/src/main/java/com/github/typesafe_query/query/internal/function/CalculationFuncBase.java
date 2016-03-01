package com.github.typesafe_query.query.internal.function;

import java.util.Objects;

import com.github.typesafe_query.enums.IntervalUnit;
import com.github.typesafe_query.meta.NumberDBColumn;
import com.github.typesafe_query.query.Func;
import com.github.typesafe_query.query.QueryContext;

public class CalculationFuncBase implements Func {
	private Number expr;
	private NumberDBColumn<?> column;
	private IntervalUnit unit;
	protected String symbol;
	
	public CalculationFuncBase(Number expr){
		this.expr = expr;
	}
	
	public CalculationFuncBase(NumberDBColumn<?> column){
		this.column = column;
	}
	
	public CalculationFuncBase(Number expr, IntervalUnit unit){
		this.expr = expr;
		this.unit = unit;
	}
	
	public CalculationFuncBase(NumberDBColumn<?> column, IntervalUnit unit){
		this.column = column;
		this.unit = unit;
	}
	
	@Override
	public String getSQL(QueryContext context, 
			String expression) {
		if(this.unit != null){
			if(this.expr != null){
				return String.format("%s %s INTERVAL %s %s", Objects.requireNonNull(expression), this.symbol, this.expr, this.unit.getString());
			}
			else if(this.column != null){
				return String.format("%s %s INTERVAL %s %s", Objects.requireNonNull(expression), this.symbol, context.getColumnPath(this.column), this.unit.getString());
			}
			return Objects.requireNonNull(expression);
		}
		else if(this.expr != null){
			return String.format("%s %s %s", Objects.requireNonNull(expression), this.symbol, this.expr);
		}
		else if(this.column != null){
			return String.format("%s %s %s", Objects.requireNonNull(expression), this.symbol, context.getColumnPath(this.column));
		}
		return Objects.requireNonNull(expression);
	}
}
