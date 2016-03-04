/**
 * 
 */
package com.github.typesafe_query.query;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.github.typesafe_query.meta.BooleanDBColumn;
import com.github.typesafe_query.meta.DBColumn;
import com.github.typesafe_query.meta.DBTable;
import com.github.typesafe_query.meta.DateDBColumn;
import com.github.typesafe_query.meta.NumberDBColumn;
import com.github.typesafe_query.meta.StringDBColumn;

/**
 * Selectクエリをあらわします。
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public interface TypesafeQuery extends SQLQuery{
	TypesafeQuery select();
	TypesafeQuery select(DBColumn<?>... columns);
	
	TypesafeQuery from(DBTable root);
	TypesafeQuery from(DBTable root,String alias);
	TypesafeQuery distinct();
	TypesafeQuery forUpdate();
	
	Join<TypesafeQuery> innerJoin(DBTable joinTable);
	Join<TypesafeQuery> innerJoin(DBTable joinTable,String alias);

	Join<TypesafeQuery> outerJoin(DBTable joinTable);
	Join<TypesafeQuery> outerJoin(DBTable joinTable,String alias);

	TypesafeQuery where(Exp... exps);
	TypesafeQuery groupBy(DBColumn<?>...columns);
	TypesafeQuery orderBy(Order...orders);
	TypesafeQuery having(Exp... exps);
	
	TypesafeQuery limit(Integer limit);
	TypesafeQuery offset(Integer offset);

	TypesafeQuery union(TypesafeQuery query);
	TypesafeQuery unionAll(TypesafeQuery query);
	
	//サブクエリ用
	DBTable as(String alias);
	StringDBColumn asStringColumn();
	
	//LocalDate系カラム
	DateDBColumn<LocalDate> asLocalDateColumn();
	DateDBColumn<LocalTime> asLocalTimeColumn();
	DateDBColumn<LocalDateTime> asLocalDateTimeColumn();
	
	DateDBColumn<Date> asDateColumn();
	DateDBColumn<Timestamp> asTimestampColumn();
	DateDBColumn<Time> asTimeColumn();
	BooleanDBColumn asBooleanColumn();
	
	//Number系カラム
	<N extends Number & Comparable<? super N>> NumberDBColumn<N> asNumberColumn();
	NumberDBColumn<Short> asShortColumn();
	NumberDBColumn<Integer> asIntegerColumn();
	NumberDBColumn<Long> asLongColumn();
	NumberDBColumn<BigInteger> asBigIntegerColumn();
	NumberDBColumn<Float> asFloatColumn();
	NumberDBColumn<Double> asDoubleColumn();
	NumberDBColumn<BigDecimal> asBigDecimalColumn();
}
