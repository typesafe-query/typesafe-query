package com.github.typesafe_query;

import static com.github.typesafe_query.Q.*;
import java.util.HashMap;
import java.util.Map;

import com.github.typesafe_query.query.QueryExecutor;

public class ReusableModelHandler<T> extends DefaultModelHandler<T> implements AutoCloseable{

	private Map<String, QueryExecutor> cache;
	
	public ReusableModelHandler(ModelDescription<T> description) {
		super(description);
		cache = new HashMap<>();
	}

	@Override
	protected QueryExecutor createExecutor(String sql) {
		QueryExecutor ex = cache.get(sql);
		if(ex == null){
			ex = newExecutor(sql);
			cache.put(sql, ex);
		}
		return ex;
	}
	
	protected QueryExecutor newExecutor(String sql){
		return stringQuery(sql).forReuse();
	}
	
	protected Map<String, QueryExecutor> getCache(){
		return cache;
	}

	@Override
	public void close() {
		cache.forEach((sql,executor) -> executor.close());
	}
}
