package com.github.typesafe_query.query.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.github.typesafe_query.DBManager;
import com.github.typesafe_query.jdbc.mapper.ResultMapper;
import com.github.typesafe_query.query.BatchQueryExecutor;
import com.github.typesafe_query.query.QueryExecutor;

/**
 * @author Takahiko Sato(MOSA Architect Inc.)
 *
 */
public class DefaultBatchQueryExecutor extends AbstractQueryExecutor implements BatchQueryExecutor{
	
	private final List<Object> params;
	
	DefaultBatchQueryExecutor(String sql) {
		super(sql);
		params = new ArrayList<Object>();
	}
	
	@Override
	public QueryExecutor clearParam() {
		params.clear();
		return this;
	}

	@Override
	public QueryExecutor addParam(Object value) {
		params.add(DBManager.getJdbcValueConverter().toJdbcObject(value));
		return this;
	}
	
	@Override
	public QueryExecutor setParams(List<Object> values) {
		clearParam();
		Objects.requireNonNull(values).stream().forEach(o -> {
			addParam(o);
		});
		return this;
	}

	@Override
	public Long insert() {
		getSQLRunner().addBatch(params);
		return null;
	}

	@Override
	public int executeUpdate() {
		getSQLRunner().addBatch(params);
		return 0;
	}

	@Override
	public <R> R getRequiredResult(Class<R> modelClass) {
		throw new UnsupportedOperationException("Unsupported when batch execution mode.");
	}

	@Override
	public <R> R getRequiredResult(ResultMapper<R> mapper) {
		throw new UnsupportedOperationException("Unsupported when batch execution mode.");
	}

	@Override
	public <R> Optional<R> getResult(Class<R> modelClass) {
		throw new UnsupportedOperationException("Unsupported when batch execution mode.");
	}

	@Override
	public <R> Optional<R> getResult(ResultMapper<R> mapper) {
		throw new UnsupportedOperationException("Unsupported when batch execution mode.");
	}

	@Override
	public <R> List<R> getResultList(Class<R> modelClass) {
		throw new UnsupportedOperationException("Unsupported when batch execution mode.");
	}

	@Override
	public <R> List<R> getResultList(ResultMapper<R> mapper) {
		throw new UnsupportedOperationException("Unsupported when batch execution mode.");
	}

	@Override
	public void executeBatch() {
		getSQLRunner().executeBatch();
	}
}
