package com.github.typesafe_query.jdbc;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLRunnerFactory {
	private static Logger logger = LoggerFactory.getLogger(SQLRunnerFactory.class);
	
	private static SQLRunnerFactory instance;
	
	public static void set(SQLRunnerFactory customFactory){
		if(instance != null){
			throw new IllegalStateException("Instance is already created!");
		}
		logger.info("Custom instance [{}]",customFactory.getClass().getName());
		instance = Objects.requireNonNull(customFactory);
	}
	
	public static SQLRunnerFactory get(){
		if(instance == null){
			logger.info("Default instance [{}]",SQLRunnerFactory.class.getName());
			instance = new SQLRunnerFactory();
		}
		return instance;
	}
	
	protected SQLRunnerFactory() {
		
	}
	
	public SQLRunner newSQLRunner(String sql){
		return new DefaultSQLRunner(sql);
	}
}
