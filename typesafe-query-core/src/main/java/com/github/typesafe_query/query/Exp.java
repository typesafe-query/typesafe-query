package com.github.typesafe_query.query;

/**
 * 条件をあらわすインターフェースです。
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public interface Exp{
	/**
	 * 条件の文字列表現を返します。
	 * @param context JPAコンテキスト
	 * @return 条件の文字列表現
	 */
	String getSQL(QueryContext context);
}
