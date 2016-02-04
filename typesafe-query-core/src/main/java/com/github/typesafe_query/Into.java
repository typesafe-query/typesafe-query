package com.github.typesafe_query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.typesafe_query.meta.DBColumn;

public class Into {
	private DBColumn<?>[] columns;
	
	public Into(){
		this(new DBColumn<?>[0]);
	}
	public Into(DBColumn<?>...columns){
		this.columns = columns;
	}
	
	public DBColumn<?>[] getColumns() {
		return this.columns;
	}
	public List<DBColumn<?>> getColumnList() {
		return columns != null ? Arrays.asList(this.columns) : new ArrayList<>();
	}
	public void setColumns(DBColumn<?>[] columns) {
		this.columns = columns;
	}
}
