/**
 * 
 */
package com.github.typesafe_query.query;


import com.github.typesafe_query.meta.DBColumn;
import com.github.typesafe_query.meta.DBTable;

/**
 * クエリコンテキストです。
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public interface QueryContext {
	
	/**
	 * クエリのルートテーブルを返します。
	 * @return ルートテーブル
	 */
	DBTable getRoot();
	
	/**
	 * テーブルエイリアス付きのカラム文字列を返します。
	 * @param path カラム
	 * @return カラム文字列表現
	 */
	String getColumnPath(DBColumn<?> path);
	
	/**
	 * カラムが所属するテーブルを返します。
	 * @param column カラム
	 * @return テーブル
	 */
	DBTable getFrom(DBColumn<?> column);
	
	void addFrom(DBTable table);
}
