package com.github.typesafe_query.jdbc;

import java.util.List;
import java.util.Optional;

import com.github.typesafe_query.jdbc.mapper.ResultMapper;

/**
 * TODO v0.3.x テストできるようにする。テスト用インスタンスを差し込めるように #34
 * @author Takahiko Sato(MOSA Architect Inc.)
 *
 */
public interface SQLRunner extends AutoCloseable{
	<T> Optional<T> get(List<Object> params,ResultMapper<T> mapper);

	<T> List<T> getList(List<Object> params,ResultMapper<T> mapper);

	int executeUpdate(List<Object> params);

	Long insertWithGeneratedKey(List<Object> params);

	void addBatch(List<Object> params);

	void executeBatch();

	@Override
	void close();
}
