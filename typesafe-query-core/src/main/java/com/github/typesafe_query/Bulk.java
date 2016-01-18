package com.github.typesafe_query;

import java.util.Set;
import com.github.typesafe_query.query.Exp;

public interface Bulk {
	int update(Set<Exp> sets);
	int updateWhere(Set<Exp> sets,Exp... expressions);
	int delete();
	int deleteWhere(Exp... expressions);
}
