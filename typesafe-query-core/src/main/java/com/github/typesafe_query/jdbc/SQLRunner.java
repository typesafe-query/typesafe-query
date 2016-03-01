package com.github.typesafe_query.jdbc;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.github.typesafe_query.jdbc.mapper.ResultMapper;

/**
 * TODO v0.3.x テストできるようにする。テスト用インスタンスを差し込めるように #34
 * @author Takahiko Sato(MOSA Architect Inc.)
 *
 */
public interface SQLRunner extends AutoCloseable{
	<T> Optional<T> get(List<JDBCTypeObject> params,ResultMapper<T> mapper);

	<T> List<T> getList(List<JDBCTypeObject> params,ResultMapper<T> mapper);
	
	<T> void fetch(List<JDBCTypeObject> params,ResultMapper<T> mapper,Predicate<T> p);

	int executeUpdate(List<JDBCTypeObject> params);

	Long insertWithGeneratedKey(List<JDBCTypeObject> params);

	void addBatch(List<JDBCTypeObject> params);

	void executeBatch();

	@Override
	void close();
}
