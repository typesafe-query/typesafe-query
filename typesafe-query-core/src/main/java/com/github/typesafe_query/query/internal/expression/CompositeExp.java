/**
 * 
 */
package com.github.typesafe_query.query.internal.expression;

import java.util.ArrayList;
import java.util.List;

import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.QueryContext;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public abstract class CompositeExp implements Exp {
	private Exp[] exps;

	public CompositeExp(Exp[] exps) {
		if(exps == null){
			throw new NullPointerException("IExp[] is null!");
		}
		this.exps = exps;
	}

	@Override
	public String getSQL(QueryContext context) {
		List<String> list = new ArrayList<String>();
		for(Exp e : exps){
			String s = e.getSQL(context);
			if(s != null){
				list.add(s);
			}
		}
		
		if(list.isEmpty()){
			return null;
		}
		
		if(list.size() == 1){
			return list.get(0);
		}
		
		return getSQL(list);
	}

	protected abstract String getSQL(List<String> list);
}
