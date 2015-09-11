/**
 * 
 */
package com.github.typesafe_query.query;


import com.github.typesafe_query.meta.IDBColumn;
import com.github.typesafe_query.meta.IDBTable;

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
	IDBTable getRoot();
	
	/**
	 * テーブルエイリアス付きのカラム文字列を返します。
	 * @param path カラム
	 * @return カラム文字列表現
	 */
	String getColumnPath(IDBColumn<?> path);
	
	/**
	 * カラムが所属するテーブルを返します。
	 * @param column カラム
	 * @return テーブル
	 */
	IDBTable getFrom(IDBColumn<?> column);
	
	void addFrom(IDBTable table);
}
