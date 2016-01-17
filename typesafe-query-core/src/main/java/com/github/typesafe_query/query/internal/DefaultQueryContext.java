/**
 * 
 */
package com.github.typesafe_query.query.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.github.typesafe_query.meta.DBColumn;
import com.github.typesafe_query.meta.DBTable;
import com.github.typesafe_query.query.InvalidQueryException;
import com.github.typesafe_query.query.QueryContext;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class DefaultQueryContext implements QueryContext {
	
	private static final String KEY_ALIAS_NONE = "ALIAS_NONE";
	
	private DBTable root;
	
	private Map<String, Map<String,DBTable>> fromMap;
	
	public DefaultQueryContext(DBTable root) {
		super();
		fromMap = new HashMap<String, Map<String,DBTable>>();
		this.root = Objects.requireNonNull(root);
		addFrom(root);
	}

	@Override
	public DBTable getRoot() {
		return root;
	}

	@Override
	public String getColumnPath(DBColumn<?> column) {
		DBTable table = getFrom(column);
		String columnName;
		if(table == null){
			//サブクエリ
			columnName = column.getExpression(this, String.format("%s",column.getName()));
		}else{
			columnName = column.getExpression(this, String.format("%s.%s", table.getAlias(),column.getName()));
		}
		
		if(column.getOtherName() != null){
			columnName = columnName + " AS " + column.getOtherName();
		}
		return columnName;
	}

	@Override
	public DBTable getFrom(DBColumn<?> column) {
		if(column == null){
			throw new NullPointerException("column is null");
		}
		DBTable table = column.getTable();
		if(table == null){
			return null;
		}
		
		Map<String,DBTable> froms = fromMap.get(table.getName());
		if(froms == null){
			throw new InvalidQueryException(String.format("指定されたカラムが属するテーブルがfrom句、join句に含まれていません。[%s]",column.getName()));
		}
		
		String key = table.getAlias();
		if(key == null){
			key = KEY_ALIAS_NONE;
		}
		
		DBTable from = froms.get(key);
		if(from == null){
			throw new InvalidQueryException(String.format("指定されたカラムが属するエイリアス指定されたテーブルがfrom句、join句,サブクエリに含まれていません。[%s.%s]",key,column.getName()));
		}
		return from;
	}
	
	@Override
	public void addFrom(DBTable table){
		Map<String, DBTable> froms = fromMap.get(table.getName());
		if(froms == null){
			froms = new HashMap<String, DBTable>();
			fromMap.put(table.getName(), froms);
		}
		String key = table.getAlias();
		DBTable dup = froms.get(key);
		if(dup != null){
			throw new InvalidQueryException("already added key " + key);
		}
		
		froms.put(key, table);
	}
}
