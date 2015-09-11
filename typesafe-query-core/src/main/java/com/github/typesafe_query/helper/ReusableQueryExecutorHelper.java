package com.github.typesafe_query.helper;

import java.util.Objects;
import java.util.function.Consumer;

import com.github.typesafe_query.Beta;
import com.github.typesafe_query.query.internal.ReusableQueryExecutor;

@Beta
public class ReusableQueryExecutorHelper {
	
	private final ReusableQueryExecutor executor;
	
	public ReusableQueryExecutorHelper(ReusableQueryExecutor executor) {
		this.executor = Objects.requireNonNull(executor);
	}
	
	public void execute(Consumer<ReusableQueryExecutor> consumer){
		Objects.requireNonNull(consumer).accept(executor);
	}
}
