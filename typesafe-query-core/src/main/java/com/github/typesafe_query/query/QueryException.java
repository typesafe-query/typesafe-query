package com.github.typesafe_query.query;


/**
 * 不正なクエリが作成されたときスローされます。
 * 
 * @author Takahiko Sato(MOSA architect Inc.)
 */
public class QueryException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * メッセージ、例外を指定して新しいインスタンスを生成します。
	 * @param message メッセージ
	 * @param couse 例外
	 */
	public QueryException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * メッセージを指定して新しいインスタンスを生成します。
	 * @param message メッセージ
	 */
	public QueryException(String message) {
		super(message);
	}

	/**
	 * 例外を指定して新しいインスタンスを生成します。
	 * @param couse 例外
	 */
	public QueryException(Throwable cause) {
		super(cause);
	}
}
