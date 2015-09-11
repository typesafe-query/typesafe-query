package com.github.typesafe_query;

import static com.github.typesafe_query.Q.*;
import com.github.typesafe_query.meta.IDBTable;
import com.github.typesafe_query.query.BatchQueryExecutor;
import com.github.typesafe_query.query.QueryExecutor;

@Beta
public class BatchModelHandler<T> extends ReusableModelHandler<T>{

	public BatchModelHandler(Class<T> modelClass, IDBTable table, ModelDescription description) {
		super(modelClass, table, description);
	}

	@Override
	protected QueryExecutor newExecutor(String sql) {
		return stringQuery(sql).forBatch();
	}
	
	public void executeBatch(){
		getCache().forEach((sql,executor) -> ((BatchQueryExecutor)executor).executeBatch());
	}
}
