/**
 * 
 */
package com.github.typesafe_query.query.internal.expression;

import java.util.List;

import com.github.typesafe_query.query.Exp;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class AndExp extends CompositeExp {
	public AndExp(Exp[] exps) {
		super(exps);
	}

	@Override
	protected String getSQL(List<String> list) {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(joinWith(getJoinString(), list));
		sb.append(")");
		
		return sb.toString();
	}
	
	protected String getJoinString(){
		return " AND ";
	}

	private static String joinWith(String ch,List<String> list){
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for(String s : list){
			if(count > 0){
				sb.append(ch);
			}
			sb.append(s);
			count++;
		}
		return sb.toString();
	}
	
}
