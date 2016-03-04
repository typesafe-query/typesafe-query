package com.github.typesafe_query.jdbc.mapper;

import java.sql.SQLException;

import com.github.typesafe_query.jdbc.ResultData;

@FunctionalInterface
public interface ResultMapper<R> {
	R map(ResultData rs) throws SQLException;
}
