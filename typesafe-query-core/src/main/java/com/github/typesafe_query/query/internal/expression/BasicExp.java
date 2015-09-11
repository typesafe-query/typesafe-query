/**
 * 
 */
package com.github.typesafe_query.query.internal.expression;


import com.github.typesafe_query.meta.IDBColumn;
import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.Param;
import com.github.typesafe_query.query.QueryContext;
import com.github.typesafe_query.query.TypesafeQuery;
import com.github.typesafe_query.query.internal.QueryUtils;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public abstract class BasicExp<T> implements Exp {
	private IDBColumn<T> left;
	private IDBColumn<T> right;
	private T rightObject;
	private Param param;
	private TypesafeQuery subQuery;
	
	public BasicExp(IDBColumn<T> left,IDBColumn<T> right) {
		if(left == null || right == null){
			throw new NullPointerException();
		}
		
		this.left = left;
		this.right = right;
	}
	
	public BasicExp(IDBColumn<T> left, T right) {
		if(left == null){
			throw new NullPointerException();
		}
		
		this.left = left;
		this.rightObject = right;
	}
	
	public BasicExp(IDBColumn<T> left, Param right) {
		if(left == null){
			throw new NullPointerException();
		}
		
		this.left = left;
		this.param = right;
	}
	
	public BasicExp(IDBColumn<T> left,TypesafeQuery right) {
		if(left == null){
			throw new NullPointerException();
		}
		
		this.left = left;
		this.subQuery = right;
	}
	
	@Override
	public String getSQL(QueryContext context) {
		String l = context.getColumnPath(left);
		String r = null;
		
		if(right != null){
			r = context.getColumnPath(right);
		}else if(rightObject != null){
			r = QueryUtils.literal(rightObject);
		}else if(param != null){
			r = param.getParameterMarker();
		}else if(subQuery != null){
			r = "(" + subQuery.getSQL(context) + ")";
		}
		
		if(r == null){
			return null;
		}
		return getSQL(l, r);
	}
	
	protected abstract String getSQL(String l,String r);

}
