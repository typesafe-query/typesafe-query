package com.github.typesafe_query.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MapResultMapper implements ResultMapper<Map<String, Object>>{
	
	@Override
	public Map<String, Object> map(ResultSet rs) throws SQLException{
		ResultSetMetaData meta = rs.getMetaData();
		Map<String, Object> map = new HashMap<String, Object>();
		for(int i = 0;i < meta.getColumnCount(); i++){
			map.put(meta.getColumnLabel(i + 1), rs.getObject(i + 1));
		}
		return map;
	}
}
