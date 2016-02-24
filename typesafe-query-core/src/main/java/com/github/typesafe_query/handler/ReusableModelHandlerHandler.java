package com.github.typesafe_query.handler;

import java.util.function.Consumer;
import java.util.function.Function;

import com.github.typesafe_query.ReusableModelHandler;

//FIXME 名前ダサ男
public class ReusableModelHandlerHandler<T> {
	
	private ReusableModelHandler<T> handler;
	
	public ReusableModelHandlerHandler(ReusableModelHandler<T> handler){
		this.handler = handler;
	}
	
	public void execute(Consumer<ReusableModelHandler<T>> consumer){
		try {
			consumer.accept(handler);
		} finally {
			handler.close();
		}
	}
	
	public <R> R execute(Function<ReusableModelHandler<T>,R> consumer){
		try {
			return consumer.apply(handler);
		} finally {
			handler.close();
		}
	}
}
