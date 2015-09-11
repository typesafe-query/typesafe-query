package com.github.typesafe_query.query.internal;

import java.util.List;
import java.util.stream.Collectors;

import com.github.typesafe_query.DBManager;

public final class QueryUtils {
	private QueryUtils() {}
	
	public static String joinWith(String ch,List<String> list){
		return list.stream().collect(Collectors.joining(ch));
	}
	
	public static String literal(Object o){
		return DBManager.getJdbcValueConverter().toLiteral(o);
	}
}
