/**
 * 
 */
package com.github.typesafe_query.query.internal.expression;

import com.github.typesafe_query.meta.IDBColumn;
import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.Param;
import com.github.typesafe_query.query.QueryContext;
import com.github.typesafe_query.query.internal.QueryUtils;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class BetweenExp<T extends Comparable<? super T>> implements Exp {
	private IDBColumn<T> left;
	private IDBColumn<T> from;
	private IDBColumn<T> to;
	private T fromObject;
	private T toObject;
	private Param fromParam;
	private Param toParam;

	public BetweenExp(IDBColumn<T> left, IDBColumn<T> from, IDBColumn<T> to) {
		if(left == null || from == null || to == null){
			throw new NullPointerException();
		}
		this.left = left;
		this.from = from;
		this.to = to;
	}
	
	public BetweenExp(IDBColumn<T> left, IDBColumn<T> from, T toObject) {
		if(left == null || from == null){
			throw new NullPointerException();
		}
		this.left = left;
		this.from = from;
		this.toObject = toObject;
	}
	
	public BetweenExp(IDBColumn<T> left, T fromObject, IDBColumn<T> to) {
		if(left == null || to == null){
			throw new NullPointerException();
		}
		this.left = left;
		this.fromObject = fromObject;
		this.to = to;
	}
	
	public BetweenExp(IDBColumn<T> left, T fromObject, T toObject) {
		if(left == null){
			throw new NullPointerException();
		}
		this.left = left;
		this.fromObject = fromObject;
		this.toObject = toObject;
	}
	
	public BetweenExp(IDBColumn<T> left, T fromObject, Param toParam) {
		if(left == null){
			throw new NullPointerException();
		}
		this.left = left;
		this.fromObject = fromObject;
		this.toParam = toParam;
	}
	
	public BetweenExp(IDBColumn<T> left, Param fromParam, T toObject) {
		if(left == null){
			throw new NullPointerException();
		}
		this.left = left;
		this.fromParam = fromParam;
		this.toObject = toObject;
	}
	
	public BetweenExp(IDBColumn<T> left, Param fromParam, IDBColumn<T> to) {
		if(left == null || to == null){
			throw new NullPointerException();
		}
		this.left = left;
		this.fromParam = fromParam;
		this.to = to;
	}
	
	public BetweenExp(IDBColumn<T> left, IDBColumn<T> from, Param toParam) {
		if(left == null || to == null){
			throw new NullPointerException();
		}
		this.left = left;
		this.from = from;
		this.toParam = toParam;
	}

	public BetweenExp(IDBColumn<T> left, Param fromParam, Param toParam) {
		if(left == null || to == null){
			throw new NullPointerException();
		}
		this.left = left;
		this.fromParam = fromParam;
		this.toParam = toParam;
	}

	
	@Override
	public String getSQL(QueryContext context) {
		String l = context.getColumnPath(left);
		String f = null;
		if(from != null){
			f = context.getColumnPath(from);
		}else if(fromObject != null){
			f = QueryUtils.literal(fromObject);
		}else if(fromParam != null){
			f = fromParam.getParameterMarker();
		}
		
		String t = null;
		if(to != null){
			t = context.getColumnPath(to);
		}else if(toObject != null){
			t = QueryUtils.literal(toObject);
		}else if(toParam != null){
			t = toParam.getParameterMarker();
		}
		
		if(f == null || t == null){
			return null;
		}
		
		return getSQL(l, f, t);
	}
	
	protected String getSQL(String l,String f,String t){
		return String.format("%s BETWEEN %s AND %s", l,f,t);
	}
}
