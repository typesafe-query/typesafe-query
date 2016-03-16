package com.github.typesafe_query.meta.impl;

import com.github.typesafe_query.meta.DBColumn;
import com.github.typesafe_query.meta.DBTable;
import com.github.typesafe_query.meta.LobDBColumn;

public class LobDBColumnImpl<T> extends DBColumnImpl<T> implements LobDBColumn<T>{

	public LobDBColumnImpl(DBTable table, String name) {
		super(table, name);
	}
	
	protected LobDBColumnImpl(DBColumn<?> wrap, String otherName) {
		super(wrap, otherName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V extends DBColumn<T>> V createFromTableAlias(String tableAlias) {
		return (V)new LobDBColumnImpl<T>(new DBTableImpl(getTable().getSchema(), getTable().getSimpleName(), tableAlias), getName());
	}

	@Override
	public DBColumn<T> as(String otherName) {
		return new LobDBColumnImpl<T>(this, otherName);
	}

}
