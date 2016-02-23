package com.github.typesafe_query.query.handler;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import com.github.typesafe_query.query.BatchQueryExecutor;

public class BatchQueryHandler {
	private final BatchQueryExecutor executor;
	
	public BatchQueryHandler(BatchQueryExecutor executor) {
		this.executor = Objects.requireNonNull(executor);
	}
	
	public void execute(Consumer<BatchQueryExecutor> consumer){
		Objects.requireNonNull(consumer).accept(executor);
		executor.executeBatch();
	}
	
	public <R> R execute(Function<BatchQueryExecutor,R> consumer){
		R r = Objects.requireNonNull(consumer).apply(executor);
		executor.executeBatch();
		return r;
	}
}
