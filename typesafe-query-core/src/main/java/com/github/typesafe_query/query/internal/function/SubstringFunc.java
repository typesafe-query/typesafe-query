/**
 * 
 */
package com.github.typesafe_query.query.internal.function;

import com.github.typesafe_query.meta.NumberDBColumn;
import com.github.typesafe_query.query.Func;
import com.github.typesafe_query.query.QueryContext;
import com.github.typesafe_query.query.internal.QueryUtils;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class SubstringFunc implements Func {
	
	private NumberDBColumn<Integer> fromCol;
	private NumberDBColumn<Integer> toCol;
	
	private Integer from;
	private Integer to;
	
	public SubstringFunc(NumberDBColumn<Integer> fromCol) {
		this.fromCol = fromCol;
	}

	public SubstringFunc(NumberDBColumn<Integer> fromCol,
			NumberDBColumn<Integer> toCol) {
		super();
		this.fromCol = fromCol;
		this.toCol = toCol;
	}
	
	public SubstringFunc(Integer from) {
		this.from = from;
	}
	
	public SubstringFunc(Integer from,Integer to) {
		this.from = from;
		this.to = to;
	}


	@Override
	public String getSQL(QueryContext context,
			String expression) {
		
		String fromExp = null;
		if(fromCol != null){
			fromExp = context.getColumnPath(fromCol);
		}else if(from != null){
			fromExp = QueryUtils.literal(from);
		}
		
		if(fromExp == null){
			return expression;
		}
		
		String toExp = null;
		if(toCol != null){
			toExp = context.getColumnPath(toCol);
		}else if(to != null){
			toExp = QueryUtils.literal(to);
		}
		
		return toExp != null?"SUBSTR(" + expression + "," + fromExp + "," + toExp + ")":"SUBSTR(" + expression + "," + fromExp + ")";
	}
}
