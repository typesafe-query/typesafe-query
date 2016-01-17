/**
 * 
 */
package com.github.typesafe_query.query.internal.expression;

import java.util.ArrayList;
import java.util.List;

import com.github.typesafe_query.meta.DBColumn;
import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.QueryContext;
import com.github.typesafe_query.query.TypesafeQuery;
import com.github.typesafe_query.query.internal.QueryUtils;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 */
public class InExp<T extends Comparable<? super T>> implements Exp {
	private DBColumn<T> left;
	private T[] objects;
	private TypesafeQuery query;
	
	public InExp(DBColumn<T> left,T[] objects) {
		if(left == null || objects == null){
			throw new NullPointerException();
		}
		this.left = left;
		this.objects = objects;
	}
	
	public InExp(DBColumn<T> left,TypesafeQuery query) {
		if(left == null || query == null){
			throw new NullPointerException();
		}
		this.left = left;
		this.query = query;
	}
	
	@Override
	public String getSQL(QueryContext context) {
		
		if(query != null){
			List<String> list = new ArrayList<String>();
			list.add(query.getSQL(context));
			return getSQL(context.getColumnPath(left), list);
		}
		
		List<String> expressions = new ArrayList<String>();
		for(Object o : objects){
			if(o != null){
				expressions.add(QueryUtils.literal(o));
			}
		}
		
		if(expressions.isEmpty()){
			return null;
		}
		return getSQL(context.getColumnPath(left), expressions);
	}
	
	protected String getSQL(String l,List<String> expressions){
		return String.format("%s IN(%s)", l, QueryUtils.joinWith(",", expressions));
	}
}
