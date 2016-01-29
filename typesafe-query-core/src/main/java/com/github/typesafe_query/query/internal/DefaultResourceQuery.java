/**
 * 
 */
package com.github.typesafe_query.query.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.github.typesafe_query.Settings;
import com.github.typesafe_query.query.NamedQuery;
import com.github.typesafe_query.query.QueryException;
import com.github.typesafe_query.query.ResourceQuery;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class DefaultResourceQuery extends DefaultStringQuery implements NamedQuery,ResourceQuery {
	
	private String name;
	
	public DefaultResourceQuery(String name) {
		super();
		this.name = name;
		
		InputStream in = getClass().getResourceAsStream(name);
		if(in == null){
			throw new QueryException("ファイルが見つかりません :" + name);
		}
		
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try{
			br = new BufferedReader(new InputStreamReader(in,Settings.get().getResourceQueryCharset()));
			String line;
			while((line = br.readLine()) != null){
				line = line.trim();
				if(!line.startsWith("--")){
					sb.append(line).append(" ");
				}
			}
			
			String sql = sb.toString();
			//空白を取り除く
			while(sql.contains("  ")){
				sql = sql.replaceAll("  ", " ");
			}
			
			addQuery(sql);
		} catch (IOException e) {
			throw new QueryException("ファイルの読み込みに失敗しました :" + name,e);
		} finally{
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					throw new QueryException("ファイルの読み込みに失敗しました :" + name,e);
				}
			}
		}
	}

	@Override
	public String getName() {
		return name;
	}
}
