package com.github.typesafe_query.query.internal.sql;

import com.github.typesafe_query.meta.IDBTable;
import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.SQLBuilder;
import com.github.typesafe_query.query.internal.DefaultQueryContext;

public class DefaultSQLBuilder implements SQLBuilder{

	@Override
	public String createDeleteSQL(IDBTable root, Exp exp) {
		StringBuilder sb = new StringBuilder();
		sb
			.append("DELETE FROM ")
			.append(root.getName())
			.append(" ")
			.append(root.getAlias());
		if(exp != null){
			DefaultQueryContext context = new DefaultQueryContext(root);
			String p = exp.getSQL(context);
			if(p != null && !p.isEmpty()){
				sb
				.append(" WHERE ")
				.append(exp.getSQL(context));
			}
		}
		return sb.toString();
	}
}
