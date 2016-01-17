package com.github.typesafe_query.query;

import com.github.typesafe_query.meta.DBTable;

public interface SQLBuilder {
	String createDeleteSQL(DBTable root,Exp exp);
}
