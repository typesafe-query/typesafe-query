package com.github.typesafe_query;

public interface ModelHandler<T> {
	Long createByGeneratedKey(T model);
	boolean create(T model);
	boolean save(T model);
	boolean delete(T model);
}
