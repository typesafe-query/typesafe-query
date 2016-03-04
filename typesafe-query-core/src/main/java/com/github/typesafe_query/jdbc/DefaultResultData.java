package com.github.typesafe_query.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Objects;

import com.github.typesafe_query.query.QueryException;

public class DefaultResultData implements ResultData{
	
	private ResultSet rs;
	
	DefaultResultData(ResultSet rs) {
		this.rs = Objects.requireNonNull(rs);
	}

	@Override
	public ResultSetMetaData getMetaData() {
		try {
			return rs.getMetaData();
		} catch (SQLException e) {
			throw new QueryException(e);
		}
	}

	@Override
	public Object get(int i) {
		try {
			return rs.getObject(i);
		} catch (SQLException e) {
			throw new QueryException(e);
		}
	}

	@Override
	public Object get(String name) {
		try {
			return rs.getObject(name);
		} catch (SQLException e) {
			throw new QueryException(e);
		}
	}

	@Override
	public String getString(String name) {
		try {
			return rs.getString(name);
		} catch (SQLException e) {
			throw new QueryException(e);
		}
	}
}
