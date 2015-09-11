package com.github.typesafe_query.query;


/**
 * Order By句をあわらすインターフェースです。
 * @author Takahiko Sato(MOSA architect Inc.)
 */
public interface Order {
	
	/**
	 * ソート順をあらわす列挙型です。
	 * @author Takahiko Sato(MOSA Architect Inc.)
	 *
	 */
	public static enum Type{
		/** 昇順 */
		ASC("asc"),
		/** 降順 */
		DESC("desc");
		private String queryString;
		private Type(String queryString) {
			this.queryString = queryString;
		}
		public String toQueryString(){
			return queryString;
		}
	}
	
	/**
	 * ORDER BY 句の文字列表現を返します。
	 * @param context クエリコンテキスト
	 * @return ORDER BY句の文字列表現
	 */
	String getOrder(QueryContext context);
}
