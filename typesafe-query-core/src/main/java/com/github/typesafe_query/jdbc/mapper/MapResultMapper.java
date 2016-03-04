package com.github.typesafe_query.jdbc.mapper;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.github.typesafe_query.jdbc.ResultData;

public class MapResultMapper implements ResultMapper<Map<String, Object>>{
	
	@Override
	public Map<String, Object> map(ResultData rd) throws SQLException{
		ResultSetMetaData meta = rd.getMetaData();
		Map<String, Object> map = new HashMap<String, Object>();
		for(int i = 0;i < meta.getColumnCount(); i++){
			map.put(meta.getColumnLabel(i + 1), rd.get(i + 1));
		}
		return map;
	}
}
