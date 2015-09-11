package com.github.typesafe_query.query.internal;

import java.util.List;
import java.util.Optional;

import com.github.typesafe_query.jdbc.mapper.ResultMapper;

/**
 * QueryRunner自体はすぐに作成し、何かしら実行後に必ずクローズ。再び使おうとしたらエラーを投げる。
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class SimpleQueryExecutor extends ReusableQueryExecutor{

	SimpleQueryExecutor(String sql) {
		super(sql);
	}

	@Override
	public <R> Optional<R> getResult(ResultMapper<R> mapper) {
		try {
			return super.getResult(mapper);
		} finally {
			getSQLRunner().close();
		}
	}

	@Override
	public <R> List<R> getResultList(ResultMapper<R> mapper) {
		try {
			return super.getResultList(mapper);
		} finally {
			getSQLRunner().close();
		}
	}

	@Override
	public Long insert() {
		try {
			return super.insert();
		} finally {
			getSQLRunner().close();
		}
	}

	@Override
	public int executeUpdate() {
		try {
			return super.executeUpdate();
		} finally {
			getSQLRunner().close();
		}
	}
}
