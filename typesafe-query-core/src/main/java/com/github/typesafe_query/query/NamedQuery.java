package com.github.typesafe_query.query;


/**
 * @author Takahiko Sato(MOSA architect Inc.)
 * @deprecated このクラスはv1.0.0までに削除されます。{@link ResourceQuery}を使用して下さい。
 */
@Deprecated
public interface NamedQuery extends StringQuery{
	/**
	 * NamedQueryの名称を返します。
	 * @return 名称
	 */
	String getName();
}
