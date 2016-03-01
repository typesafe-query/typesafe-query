package com.github.typesafe_query.query.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.github.typesafe_query.DBManager;
import com.github.typesafe_query.jdbc.JDBCTypeObject;
import com.github.typesafe_query.jdbc.convert.TypeConverter;
import com.github.typesafe_query.jdbc.mapper.BeanResultMapper;
import com.github.typesafe_query.jdbc.mapper.ResultMapper;
import com.github.typesafe_query.query.QueryExecutor;
import com.github.typesafe_query.query.SQLQuery;

/**
 * FIXME v0.x.x TypesafeもstringもModelも全部再利用できるやーつ。ほしい。 #23
 * FIXME v0.x.x Query系は適宜reuse()とか呼べ！！Model系はラップしてやんよ。的なスタンスでいいのか？ #23
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
	
	private final List<JDBCTypeObject> params;
	
	public ReusableQueryExecutor(SQLQuery sqlQuery) {
		super(sqlQuery);
		params = new ArrayList<>();
	}
	
	@Override
	public QueryExecutor clearParam() {
		params.clear();
		return this;
	}

	@Override
	public QueryExecutor addParam(Object value) {
		params.add(DBManager.getDialectTranslator().toJdbcTypeObject(value));
		return this;
	}

	@Override
	public QueryExecutor addParam(Object value,TypeConverter converter) {
		params.add(DBManager.getDialectTranslator().toJdbcTypeObject(value,converter));
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
	public <R> void fetch(Class<R> modelClass, Predicate<R> p) {
		getSQLRunner().fetch(params, new BeanResultMapper<R>(modelClass), p);
	}

	@Override
	public <R> void fetch(ResultMapper<R> mapper, Predicate<R> p) {
		getSQLRunner().fetch(params, mapper, p);
	}
	
	@Override
	public <R> void fetch(Class<R> modelClass, Consumer<R> p) {
		fetch(modelClass, r -> {
			p.accept(r);
			return true;
		});
	}

	@Override
	public <R> void fetch(ResultMapper<R> mapper, Consumer<R> p) {
		fetch(mapper, r -> {
			p.accept(r);
			return true;
		});
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
