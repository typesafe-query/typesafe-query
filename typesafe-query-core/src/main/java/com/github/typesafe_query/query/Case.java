package com.github.typesafe_query.query;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import com.github.typesafe_query.meta.IBooleanDBColumn;
import com.github.typesafe_query.meta.IDateDBColumn;
import com.github.typesafe_query.meta.INumberDBColumn;
import com.github.typesafe_query.meta.IStringDBColumn;

public interface Case {
	String getSQL(QueryContext context);
	IStringDBColumn endAsStringColumn();
	IDateDBColumn<Date> endAsDateColumn();
	IDateDBColumn<Timestamp> endAsTimestampColumn();
	IDateDBColumn<Time> endAsTimeColumn();
	IBooleanDBColumn endAsBooleanColumn();
	<N extends Number & Comparable<? super N>> INumberDBColumn<N> endAsNumberColumn();
}
