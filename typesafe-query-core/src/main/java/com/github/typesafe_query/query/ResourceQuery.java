package com.github.typesafe_query.query;


/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public interface ResourceQuery extends StringQuery{
	/**
	 * NamedQueryの名称を返します。
	 * @return 名称
	 */
	String getName();
}
