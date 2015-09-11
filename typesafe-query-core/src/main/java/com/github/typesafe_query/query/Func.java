package com.github.typesafe_query.query;


/**
 * SQL関数をあらわすクラスです
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public interface Func {
	
	/**
	 * SQL関数の文字列表現をかえします
	 * @param context JPAコンテキスト
	 * @param expression 式
	 * @return SQL関数の文字列表現
	 */
	String getSQL(QueryContext context,String expression);
}
