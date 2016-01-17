package com.github.typesafe_query.query.internal;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import com.github.typesafe_query.meta.BooleanDBColumn;
import com.github.typesafe_query.meta.DateDBColumn;
import com.github.typesafe_query.meta.NumberDBColumn;
import com.github.typesafe_query.meta.StringDBColumn;
import com.github.typesafe_query.meta.impl.BooleanDBColumnImpl;
import com.github.typesafe_query.meta.impl.DateDBColumnImpl;
import com.github.typesafe_query.meta.impl.NumberDBColumnImpl;
import com.github.typesafe_query.meta.impl.StringDBColumnImpl;
import com.github.typesafe_query.query.Case;

public abstract class DefaultCase implements Case{

	@Override
	public StringDBColumn endAsStringColumn() {
		return new StringDBColumnImpl(this);
	}

	@Override
	public DateDBColumn<Date> endAsDateColumn() {
		return new DateDBColumnImpl<Date>(this);
	}

	@Override
	public DateDBColumn<Timestamp> endAsTimestampColumn() {
		return new DateDBColumnImpl<Timestamp>(this);
	}

	@Override
	public DateDBColumn<Time> endAsTimeColumn() {
		return new DateDBColumnImpl<Time>(this);
	}

	@Override
	public BooleanDBColumn endAsBooleanColumn() {
		return new BooleanDBColumnImpl(this);
	}

	@Override
	public <N extends Number & Comparable<? super N>> NumberDBColumn<N> endAsNumberColumn() {
		return new NumberDBColumnImpl<N>(this);
	}
}
