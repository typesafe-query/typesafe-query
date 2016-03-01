/**
 * 
 */
package com.github.typesafe_query.query.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.typesafe_query.Settings;
import com.github.typesafe_query.query.NamedQuery;
import com.github.typesafe_query.query.QueryException;
import com.github.typesafe_query.query.ResourceQuery;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class DefaultResourceQuery extends DefaultStringQuery implements NamedQuery,ResourceQuery {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultResourceQuery.class);
	
	private final String path;
	private final String name;
	
	public DefaultResourceQuery(String name) {
		super();
		this.name = name;
		if(name.startsWith("/")){
			name = name.substring(1);
		}
		this.path = Settings.get().getExternalFileRoot() + name;
		
		logger.debug("Path {}",path);
		InputStream in = getClass().getResourceAsStream(path);
		if(in == null){
			throw new QueryException("ファイルが見つかりません :" + path);
		}
		
		StringBuilder sb = new StringBuilder();
		try(BufferedReader br = new BufferedReader(new InputStreamReader(in,Settings.get().getResourceQueryCharset()))){
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
		}
	}

	@Override
	public String getName() {
		return name;
	}
}
