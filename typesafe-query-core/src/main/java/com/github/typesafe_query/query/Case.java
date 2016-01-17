package com.github.typesafe_query.query;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import com.github.typesafe_query.meta.BooleanDBColumn;
import com.github.typesafe_query.meta.DateDBColumn;
import com.github.typesafe_query.meta.NumberDBColumn;
import com.github.typesafe_query.meta.StringDBColumn;

public interface Case {
	String getSQL(QueryContext context);
	StringDBColumn endAsStringColumn();
	DateDBColumn<Date> endAsDateColumn();
	DateDBColumn<Timestamp> endAsTimestampColumn();
	DateDBColumn<Time> endAsTimeColumn();
	BooleanDBColumn endAsBooleanColumn();
	<N extends Number & Comparable<? super N>> NumberDBColumn<N> endAsNumberColumn();
}
