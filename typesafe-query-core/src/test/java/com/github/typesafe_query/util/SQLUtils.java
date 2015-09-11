package com.github.typesafe_query.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SQLUtils {
	
	private static Logger logger = LoggerFactory.getLogger(SQLUtils.class);
	
	private SQLUtils() {}
	
	
	public static void executeResource(Connection con,String resource) throws IOException, SQLException{
		if(con == null || resource == null){
			throw new NullPointerException();
		}
		
		InputStream in = SQLUtils.class.getResourceAsStream(resource);
		BufferedReader br = null;
		try{
			br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
			StringBuilder sb = new StringBuilder();
			String s;
			while((s = br.readLine()) != null){
				if(s.trim().startsWith("--") || s.trim().startsWith("/*")){
					continue;
				}
				sb.append(s).append(" ");
			}
			
			String sqls = sb.toString();
			sqls = sqls.replaceAll("\r", "");
			sqls = sqls.replaceAll("\n", "");
			sqls = sqls.replaceAll("\t", " ");
			sqls = sqls.trim();
			if(sqls.endsWith(";")){
				sqls = sqls.substring(0, sqls.length() -1);
			}
			String[] sqlList = sqls.split(";");
			for(String sql : sqlList){
				execute(con, sql);
			}
		}finally{
			if(br != null){
				br.close();
			}
		}
	}
	
	public static void execute(Connection con,String sql) throws SQLException{
		if(sql == null || con == null){
			throw new NullPointerException();
		}
		logger.info(sql.trim());
		Statement s = null;
		try{
			s = con.createStatement();
			s.execute(sql);
		}finally{
			if(s != null){
				s.close();
			}
		}
	}
}
