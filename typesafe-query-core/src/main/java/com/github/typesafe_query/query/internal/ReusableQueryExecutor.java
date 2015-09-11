package com.github.typesafe_query.query.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.github.typesafe_query.DBManager;
import com.github.typesafe_query.jdbc.mapper.BeanResultMapper;
import com.github.typesafe_query.jdbc.mapper.ResultMapper;
import com.github.typesafe_query.query.QueryExecutor;

/**
 * FIXME v0.x.x TypesafeもstringもModelも全部再利用できるやーつ。ほしい。
 * FIXME v0.x.x Query系は適宜reuse()とか呼べ！！Model系はラップしてやんよ。的なスタンスでいいのか？
 * <pre>
 * List&lt;Hoge&gt; result = new ArrayList&lt;&gt;();
 * prepare(select()
 *         .from(Hoge_.TABLE)
 *         .where(Hoge_.NAME.eq(param())
 *         .reuse()
 * ).execute((executor) -&gt; {
 *     names.stream().forEach((name) -&gt; {
 *         result.add(
 *           executor
 *             .addParam(name)
 *             .getRequiredResult(Hoge.class)
 *         );
 *     });
 * });
 * </pre>
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class ReusableQueryExecutor extends AbstractQueryExecutor implements QueryExecutor{
	
	private final List<Object> params;
	
	ReusableQueryExecutor(String sql) {
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
	public <R> R getRequiredResult(Class<R> modelClass) {
		return getResult(modelClass).orElseThrow(()->new RuntimeException("Record is required but no result."));
	}

	@Override
	public <R> R getRequiredResult(ResultMapper<R> mapper) {
		return getResult(mapper).orElseThrow(()->new RuntimeException("Record is required but no result."));
	}

	@Override
	public <R> Optional<R> getResult(Class<R> modelClass) {
		return getResult(new BeanResultMapper<R>(modelClass));
	}

	@Override
	public <R> Optional<R> getResult(ResultMapper<R> mapper) {
		return getSQLRunner().get(params,mapper);
	}

	@Override
	public <R> List<R> getResultList(Class<R> modelClass) {
		return getResultList(new BeanResultMapper<R>(modelClass));
	}
	
	@Override
	public <R> List<R> getResultList(ResultMapper<R> mapper) {
		return getSQLRunner().getList(params,mapper);
	}

	@Override
	public Long insert() {
		return getSQLRunner().insertWithGeneratedKey(params);
	}

	@Override
	public int executeUpdate() {
		return getSQLRunner().executeUpdate(params);
	}
}
