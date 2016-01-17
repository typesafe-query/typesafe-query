package com.github.typesafe_query;

import java.util.Set;

import com.github.typesafe_query.meta.IDBTable;
import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.InvalidQueryException;
import com.github.typesafe_query.query.SQLQuery;
import com.github.typesafe_query.query.internal.DefaultQueryContext;

/**
 * 一括更新、一括削除をするためのクラスです。
 * TODO v0.x.x バッチ用インスタンスを作成するファクトリメソッドを追加する？ #23
 * @author Takahiko Sato(MOSA Architect Inc.)
 *
 */
public class Bulk {
	
	private IDBTable root;
	
	/**
	 * モデルクラス、テーブルを指定して新しいインスタンスを生成します。
	 * @param table テーブル
	 */
	public Bulk(IDBTable table) {
		this.root = table;
	}
	
	/**
	 * 全件更新します
	 * @param sets 更新項目
	 * @return 更新件数
	 */
	public int update(Set<Exp> sets){
		return updateWhere(sets, null);
	}
	
	/**
	 * 条件を指定して更新します
	 * @param sets 更新項目
	 * @param exp 条件
	 * @return 更新件数
	 */
	public int updateWhere(Set<Exp> sets,Exp exp){
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
		
		if(exp != null){
			sb.append(" WHERE ");
			sb.append(exp.getSQL(context));
		}
		
		String sql = sb.toString();
		SQLQuery query = Q.stringQuery(sql);
		return query.forOnce().executeUpdate();
	}
	
	/**
	 * 全件削除します
	 * @return 削除件数
	 */
	public int delete(){
		return deleteWhere(null);
	}
	
	/**
	 * 条件を指定して削除します
	 * @param exp 条件
	 * @return 削除件数
	 */
	public int deleteWhere(Exp exp){
		String sql = DBManager.getSQLBuilder().createDeleteSQL(root, exp);
		SQLQuery query = Q.stringQuery(sql);
		return query.forOnce().executeUpdate();
	}
}
