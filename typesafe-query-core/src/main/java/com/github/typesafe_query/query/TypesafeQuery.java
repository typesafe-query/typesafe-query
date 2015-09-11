/**
 * 
 */
package com.github.typesafe_query.query;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.github.typesafe_query.meta.IBooleanDBColumn;
import com.github.typesafe_query.meta.IDBColumn;
import com.github.typesafe_query.meta.IDBTable;
import com.github.typesafe_query.meta.IDateDBColumn;
import com.github.typesafe_query.meta.INumberDBColumn;
import com.github.typesafe_query.meta.IStringDBColumn;

/**
 * Selectクエリをあらわします。
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public interface TypesafeQuery extends SQLQuery{
	TypesafeQuery from(IDBTable root);
	TypesafeQuery from(IDBTable root,String alias);
	TypesafeQuery distinct();
	TypesafeQuery forUpdate();
	
	Join<TypesafeQuery> innerJoin(IDBTable joinTable);
	Join<TypesafeQuery> innerJoin(IDBTable joinTable,String alias);

	Join<TypesafeQuery> outerJoin(IDBTable joinTable);
	Join<TypesafeQuery> outerJoin(IDBTable joinTable,String alias);

	TypesafeQuery where(Exp... exps);
	TypesafeQuery groupBy(IDBColumn<?>...columns);
	TypesafeQuery orderBy(Order...orders);
	TypesafeQuery having(Exp... exps);
	
	TypesafeQuery limit(Integer limit);
	TypesafeQuery offset(Integer offset);
	
	//サブクエリ用
	IDBTable as(String alias);
	IStringDBColumn asStringColumn();
	
	//LocalDate系カラム
	IDateDBColumn<LocalDate> asLocalDateColumn();
	IDateDBColumn<LocalTime> asLocalTimeColumn();
	IDateDBColumn<LocalDateTime> asLocalDateTimeColumn();
	
	IDateDBColumn<Date> asDateColumn();
	IDateDBColumn<Timestamp> asTimestampColumn();
	IDateDBColumn<Time> asTimeColumn();
	IBooleanDBColumn asBooleanColumn();
	<N extends Number & Comparable<? super N>> INumberDBColumn<N> asNumberColumn();
}
