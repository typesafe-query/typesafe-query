package com.github.typesafe_query.jdbc;

public class JDBCTypeObject {
	
	private final Object value;
	
	public JDBCTypeObject(Object value) {
		this.value = value;
	}
	
	public Object getValue(){
		return this.value;
	}

	@Override
	public String toString() {
		return value == null?null:value.toString();
	}
}
