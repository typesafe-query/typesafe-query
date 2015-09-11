package com.github.typesafe_query.query;

import com.github.typesafe_query.meta.IDBTable;

public interface SQLBuilder {
	String createDeleteSQL(IDBTable root,Exp exp);
}
