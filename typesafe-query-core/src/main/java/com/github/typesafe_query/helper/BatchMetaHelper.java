package com.github.typesafe_query.helper;

import java.util.function.Consumer;

import com.github.typesafe_query.BatchModelHandler;
import com.github.typesafe_query.Beta;

@Beta
public class BatchMetaHelper<T> {
	
	private BatchModelHandler<T> handler;
	
	public BatchMetaHelper(BatchModelHandler<T> handler){
		this.handler = handler;
	}
	
	public void execute(Consumer<BatchModelHandler<T>> consumer){
		try {
			consumer.accept(handler);
			handler.executeBatch();
		} finally {
			handler.close();
		}
	}
}
