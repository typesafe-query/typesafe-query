package com.github.typesafe_query;

import com.github.typesafe_query.meta.DBColumn;

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
	boolean save(T model,DBColumn<?>... columns);
	boolean delete(T model);
	boolean invalid(T model);
}
