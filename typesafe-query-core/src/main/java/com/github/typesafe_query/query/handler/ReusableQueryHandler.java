package com.github.typesafe_query.query.handler;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import com.github.typesafe_query.query.internal.ReusableQueryExecutor;

public class ReusableQueryHandler {
	
	private final ReusableQueryExecutor executor;
	
	public ReusableQueryHandler(ReusableQueryExecutor executor) {
		this.executor = Objects.requireNonNull(executor);
	}
	
	public void execute(Consumer<ReusableQueryExecutor> consumer){
		Objects.requireNonNull(consumer).accept(executor);
	}
	
	public <R> R execute(Function<ReusableQueryExecutor,R> consumer){
		return Objects.requireNonNull(consumer).apply(executor);
	}

}
