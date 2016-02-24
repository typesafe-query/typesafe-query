package com.github.typesafe_query;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.typesafe_query.meta.DBColumn;
import com.github.typesafe_query.meta.DBTable;
import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.InvalidQueryException;
import com.github.typesafe_query.query.QueryExecutor;
import com.github.typesafe_query.query.TypesafeQuery;
import com.github.typesafe_query.query.internal.DefaultQueryContext;
import com.github.typesafe_query.query.internal.SimpleQueryExecutor;

/**
 * 一括更新、一括削除をするためのクラスです。
 * TODO v0.x.x バッチ用インスタンスを作成するファクトリメソッドを追加する？ #23
 * @author Takahiko Sato(MOSA Architect Inc.)
 *
 */
public class DefaultBulk implements Bulk{
	
	private DBTable root;
	
	/**
	 * モデルクラス、テーブルを指定して新しいインスタンスを生成します。
	 * @param table テーブル
	 */
	public DefaultBulk(DBTable table) {
		this.root = table;
	}
	
	/**
	 * サブクエリの結果を新規行として追加します
	 * @param subQuery サブクエリ
	 * @return 更新件数
	 */
	@Override
	public int insert(TypesafeQuery subQuery) {
		return insert(new Into(), subQuery);
	}

	/**
	 * 列を指定してサブクエリの結果を新規行として追加します
	 * @param into 指定列情報
	 * @param subQuery サブクエリ
	 * @return 更新件数
	 */
	@Override
	public int insert(Into into, TypesafeQuery subQuery){
		if(subQuery == null){
			throw new NullPointerException();
		}
		
		DefaultQueryContext context = new DefaultQueryContext(root);
		
		StringBuilder sb = new StringBuilder();
		sb
			.append("INSERT INTO ")
			.append(root.getName())
			.append(" ");
		
		List<DBColumn<?>> columns = into != null ? into.getColumnList() : new ArrayList<>();
		
		if(columns.size() > 0){
			sb.append(String.format("(%s) ", String.join(", ", columns.stream().map(v -> v.getName()).collect(Collectors.toList()))));
		}
		
		sb.append(subQuery.getSQL(context));
		
		String sql = sb.toString();
		return createExecutor(sql).executeUpdate();
	}

	/**
	 * 全件更新します
	 * @param sets 更新項目
	 * @return 更新件数
	 */
	@Override
	public int update(Set<Exp> sets){
		return updateWhere(sets);
	}
	
	/**
	 * 条件を指定して更新します
	 * @param sets 更新項目
	 * @param expressions 条件
	 * @return 更新件数
	 */
	@Override
	public int updateWhere(Set<Exp> sets,Exp...expressions){
		if(sets == null){
			throw new NullPointerException();
		}
		if(sets.isEmpty()){
			throw new InvalidQueryException("SET句に指定する項目がありません");
		}
		
		DefaultQueryContext context = new DefaultQueryContext(root);
		
		StringBuilder sb = new StringBuilder();
		sb
			.append("UPDATE ")
			.append(root.getName())
			.append(" ")
			.append(root.getAlias())
			.append(" SET ");
		boolean first = true;
		for(Exp e : sets){
			if(!first){
				sb.append(",");
			}
			sb.append(e.getSQL(context));
			first = false;
		}
		
		if(expressions != null && expressions.length > 0){
			sb.append(" WHERE ");
			Exp exp;
			if(expressions.length == 1){
				exp = expressions[0];
			}else{
				exp = Q.and(expressions);
			}
			sb.append(exp.getSQL(context));
		}
		
		String sql = sb.toString();
		return createExecutor(sql).executeUpdate();
	}
	
	/**
	 * 全件削除します
	 * @return 削除件数
	 */
	@Override
	public int delete(){
		return deleteWhere();
	}
	
	/**
	 * 条件を指定して削除します
	 * @param expressions 条件
	 * @return 削除件数
	 */
	@Override
	public int deleteWhere(Exp... expressions){
		Exp exp = null;
		if(expressions.length == 1){
			exp = expressions[0];
		}else if(expressions.length > 1){
			exp = Q.and(expressions);
		}
		String sql = DBManager.getSQLBuilder().createDeleteSQL(root, exp);
		return createExecutor(sql).executeUpdate();
	}
	
	protected QueryExecutor createExecutor(String sql){
		return new SimpleQueryExecutor(Q.stringQuery(sql));
	}
}
