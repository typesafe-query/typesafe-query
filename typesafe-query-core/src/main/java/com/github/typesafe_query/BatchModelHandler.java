package com.github.typesafe_query;

import com.github.typesafe_query.query.BatchQueryExecutor;
import com.github.typesafe_query.query.QueryExecutor;
import com.github.typesafe_query.query.internal.DefaultBatchQueryExecutor;

public class BatchModelHandler<T> extends ReusableModelHandler<T>{

	public BatchModelHandler(ModelDescription<T> description) {
		super(description);
	}

	@Override
	protected QueryExecutor newExecutor(String sql) {
		return new DefaultBatchQueryExecutor(Q.stringQuery(sql));
	}
	
	public void executeBatch(){
		getCache().forEach((sql,executor) -> ((BatchQueryExecutor)executor).executeBatch());
	}
}
