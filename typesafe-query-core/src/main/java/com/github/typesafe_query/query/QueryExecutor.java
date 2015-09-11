package com.github.typesafe_query.query;

import java.util.List;
import java.util.Optional;

import com.github.typesafe_query.jdbc.mapper.ResultMapper;

public interface QueryExecutor extends AutoCloseable{
	
	Long insert();
	int executeUpdate();

	<R> R getRequiredResult(Class<R> modelClass);
	<R> R getRequiredResult(ResultMapper<R> mapper);
	<R> Optional<R> getResult(Class<R> modelClass);
	<R> Optional<R> getResult(ResultMapper<R> mapper);
	<R> List<R> getResultList(Class<R> modelClass);
	<R> List<R> getResultList(ResultMapper<R> mapper);
	
	QueryExecutor clearParam();
	QueryExecutor addParam(Object value);
	QueryExecutor setParams(List<Object> values);
	
	void close();
}
