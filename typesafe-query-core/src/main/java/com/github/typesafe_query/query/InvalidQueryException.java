package com.github.typesafe_query.query;


/**
 * 不正なクエリが作成されたときスローされます。
 * 
 * @author Takahiko Sato(MOSA architect Inc.)
 */
public class InvalidQueryException extends QueryException {

	private static final long serialVersionUID = 1L;

	/**
	 * メッセージ、例外を指定して新しいインスタンスを生成します。
	 * @param message メッセージ
	 * @param cause 例外
	 */
	public InvalidQueryException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * メッセージを指定して新しいインスタンスを生成します。
	 * @param message メッセージ
	 */
	public InvalidQueryException(String message) {
		super(message);
	}

	/**
	 * 例外を指定して新しいインスタンスを生成します。
	 * @param cause 例外
	 */
	public InvalidQueryException(Throwable cause) {
		super(cause);
	}
}
