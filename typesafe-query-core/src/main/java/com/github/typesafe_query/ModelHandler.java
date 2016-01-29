package com.github.typesafe_query;

public interface ModelHandler<T> {
	/**
	 * このメソッドはv1.0.0までに削除されます。
	 * <p>@Id項目に@AutoIncrementを付与して、createメソッドを使用して下さい。自動的にModelのIDにセットされます</p>
	 * @deprecated
	 * @param model
	 * @return
	 */
	@Deprecated
	Long createByGeneratedKey(T model);
	boolean create(T model);
	boolean save(T model);
	boolean delete(T model);
}
