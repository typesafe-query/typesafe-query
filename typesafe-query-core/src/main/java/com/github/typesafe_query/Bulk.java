package com.github.typesafe_query;

import java.util.Set;

import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.TypesafeQuery;

public interface Bulk {
	int insert(TypesafeQuery subQuery);
	int insert(Into into, TypesafeQuery subQuery);
	int update(Set<Exp> sets);
	int updateWhere(Set<Exp> sets,Exp... expressions);
	int delete();
	int deleteWhere(Exp... expressions);
}
