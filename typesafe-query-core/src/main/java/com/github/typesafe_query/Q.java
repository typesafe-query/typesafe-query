/**
 * 
 */
package com.github.typesafe_query;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.github.typesafe_query.enums.IntervalUnit;
import com.github.typesafe_query.handler.BatchModelHandlerHandler;
import com.github.typesafe_query.handler.ReusableModelHandlerHandler;
import com.github.typesafe_query.meta.ComparableDBColumn;
import com.github.typesafe_query.meta.DBColumn;
import com.github.typesafe_query.meta.MetaClass;
import com.github.typesafe_query.meta.NumberDBColumn;
import com.github.typesafe_query.meta.StringDBColumn;
import com.github.typesafe_query.meta.impl.DateDBColumnImpl;
import com.github.typesafe_query.meta.impl.NumberDBColumnImpl;
import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.NamedQuery;
import com.github.typesafe_query.query.Param;
import com.github.typesafe_query.query.SQLQuery;
import com.github.typesafe_query.query.SearchedCase;
import com.github.typesafe_query.query.SimpleCase;
import com.github.typesafe_query.query.StringQuery;
import com.github.typesafe_query.query.TypesafeQuery;
import com.github.typesafe_query.query.TypesafeQueryFactory;
import com.github.typesafe_query.query.handler.BatchQueryHandler;
import com.github.typesafe_query.query.handler.ReusableQueryHandler;
import com.github.typesafe_query.query.internal.DefaultBatchQueryExecutor;
import com.github.typesafe_query.query.internal.DefaultResourceQuery;
import com.github.typesafe_query.query.internal.DefaultSearchedCase;
import com.github.typesafe_query.query.internal.DefaultSimpleCase;
import com.github.typesafe_query.query.internal.DefaultStringQuery;
import com.github.typesafe_query.query.internal.ParamImpl;
import com.github.typesafe_query.query.internal.ReusableQueryExecutor;
import com.github.typesafe_query.query.internal.expression.AndExp;
import com.github.typesafe_query.query.internal.expression.ExistsExp;
import com.github.typesafe_query.query.internal.expression.NotExistsExp;
import com.github.typesafe_query.query.internal.expression.OrExp;
import com.github.typesafe_query.util.ClassUtils;

/**
 * シンプルなクエリを記述するためのヘルパークラスです。
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public final class Q {
	
	private Q() {}
	
	/**
	 * {@link TypesafeQuery}を返します。
	 * @return {@link TypesafeQuery}
	 */
	public static TypesafeQuery select(){
		return TypesafeQueryFactory.get();
	}
	
	/**
	 * {@link TypesafeQuery}を返します。
	 * @param columns select対象のカラム
	 * @return {@link TypesafeQuery}
	 */
	public static TypesafeQuery select(DBColumn<?>... columns){
		return TypesafeQueryFactory.get(columns);
	}
	
	public static StringQuery queryFrom(String name){
		return new DefaultResourceQuery(name);
	}
	
	/**
	 * {@link NamedQuery}を返します。
	 * @param name クエリ名
	 * @return {@link NamedQuery}
	 * @deprecated このメソッドはv1.0.0までに削除されます。{@link Q#queryFrom(String)}を使用して下さい。
	 */
	@Deprecated
	public static NamedQuery namedQuery(String name){
		return new DefaultResourceQuery(name);
	}
	
	public static StringQuery stringQuery(String sql){
		return new DefaultStringQuery(sql);
	}
	
	public static ReusableQueryHandler reuse(SQLQuery query){
		return new ReusableQueryHandler(new ReusableQueryExecutor(query));
	}
	
	public static BatchQueryHandler batch(SQLQuery query){
		return new BatchQueryHandler(new DefaultBatchQueryExecutor(query));
	}
	
	@SuppressWarnings("unchecked")
	public static <T> ReusableModelHandlerHandler<T> reuseModel(Class<? extends MetaClass> metaClass){
		//TODO リフレクション・・・
		Method m = ClassUtils.getMethod("description", new Class[]{}, metaClass);
		ModelDescription<T> desc = (ModelDescription<T>)ClassUtils.invoke(m, null);
		return new ReusableModelHandlerHandler<>(new ReusableModelHandler<>(desc));
	}

	@SuppressWarnings("unchecked")
	public static <T> BatchModelHandlerHandler<T> batchModel(Class<? extends MetaClass> metaClass){
		//TODO リフレクション・・・
		Method m = ClassUtils.getMethod("description", new Class[]{}, metaClass);
		ModelDescription<T> desc = (ModelDescription<T>)ClassUtils.invoke(m, null);
		return new BatchModelHandlerHandler<>(new BatchModelHandler<>(desc));
	}

	public static SearchedCase case_(){
		return new DefaultSearchedCase();
	}
	
	public static <T> SimpleCase<T> case_(DBColumn<T> col){
		return new DefaultSimpleCase<T>(col);
	}

	/**
	 * {@link Param}を返します。
	 * @return {@link Param}
	 */
	public static Param param(){
		return new ParamImpl();
	}
	
	/**
	 * エイリアス付きの{@link DBColumn}を返します。
	 * @param alias エイリアス
	 * @param v DBカラム
	 * @param <T> DBカラムの型
	 * @param <V> DBカラム
	 * @return {@link DBColumn}
	 */
	
	public static <T,V extends DBColumn<T>> V $(String alias,V v){
		return v.createFromTableAlias(alias);
	}
	
	/**
	 * AND式を返します。
	 * @param exps ANDで結合する条件
	 * @return AND
	 */
	public static Exp and(Exp...exps){
		return new AndExp(exps);
	}
	
	/**
	 * OR式を返します。
	 * @param exps ORで結合する条件
	 * @return OR
	 */
	public static Exp or(Exp...exps){
		return new OrExp(exps);
	}
	
	/**
	 * EXISTS式を返します。
	 * @param exp サブクエリ
	 * @return EXISTS
	 */
	public static Exp exists(TypesafeQuery exp){
		return new ExistsExp(exp);
	}
	
	/**
	 * NOT EXISTS式を返します。
	 * @param exp サブクエリ
	 * @return NOT EXISTS
	 */
	public static Exp notExists(TypesafeQuery exp){
		return new NotExistsExp(exp);
	}
	
	//--->comparable functions
	/**
	 * coalsesce式を返します。
	 * @param r デフォルト値
	 * @param v 対象DBカラム
	 * @param <V> DBカラムの型
	 * @param <R> DBカラム
	 * @return coalsesce
	 */
	public static <V extends Comparable<? super V>,R extends ComparableDBColumn<V>> R coalsesce(R r,V v){
		return r.coalesce(v);
	}
	
	/**
	 * coalsesce式を返します。
	 * @param r デフォルトDBカラム
	 * @param v 対象DBカラム
	 * @param <V> DBカラムの型
	 * @param <R> DBカラム
	 * @return coalsesce
	 */
	public static <V extends Comparable<? super V>,R extends ComparableDBColumn<V>> R coalsesce(R r,R v){
		return r.coalesce(v);
	}

	/**
	 * COUNTを返します。
	 * @param c 対象DBカラム
	 * @return COUNT
	 */
	public static NumberDBColumn<Long> count(ComparableDBColumn<?> c){
		NumberDBColumn<Long> nc = new NumberDBColumnImpl<Long>("*".equals(c.getName()) ? null : c.getTable(), c.getName());
		return nc.count();
	}
	 
	//--->numeric functions
	/**
	 * MAXを返します。
	 * @param c 対象DBカラム
	 * @param <T> DBカラムの型
	 * @return MAX
	 */
	public static <T extends Number & Comparable<? super T>> NumberDBColumn<T> max(NumberDBColumn<T> c){
		return c.max();
	}
	
	/**
	 * MINを返します。
	 * @param c 対象DBカラム
	 * @param <T> DBカラムの型
	 * @return MIN
	 */
	public static <T extends Number & Comparable<? super T>> NumberDBColumn<T> min(NumberDBColumn<T> c){
		return c.min();
	}
	
	/**
	 * ABSを返します。
	 * @param c 対象DBカラム
	 * @return ABS
	 */
	public static NumberDBColumn<BigDecimal> abs(NumberDBColumn<?> c){
		return c.abs();
	}
	
	/**
	 * AVGを返します。
	 * @param c 対象DBカラム
	 * @return AVG
	 */
	public static NumberDBColumn<BigDecimal> avg(NumberDBColumn<?> c){
		return c.avg();
	}
	
	/**
	 * SQRTを返します。
	 * @param c 対象DBカラム
	 * @return SQRT
	 */
	public static NumberDBColumn<BigDecimal> sqrt(NumberDBColumn<?> c){
		return c.sqrt();
	}
	
	//---->string functions
	/**
	 * LOWERを返します。
	 * @param c 対象DBカラム
	 * @return LOWER
	 */
	public static StringDBColumn lower(StringDBColumn c){
		return c.lower();
	}
	
	/**
	 * UPPERを返します。
	 * @param c 対象DBカラム
	 * @return UPPER
	 */
	public static StringDBColumn upper(StringDBColumn c){
		return c.upper();
	}
	
	/**
	 * TRIMを返します。
	 * @param c 対象DBカラム
	 * @return TRIM
	 */
	public static StringDBColumn trim(StringDBColumn c){
		return c.trim();
	}
	
	/**
	 * RTRIMを返します。
	 * @param c 対象DBカラム
	 * @return RTRIM
	 */
	public static StringDBColumn rtrim(StringDBColumn c){
		return c.rtrim();
	}
	
	/**
	 * LTRIMを返します。
	 * @param c 対象DBカラム
	 * @return LTRIM
	 */
	public static StringDBColumn ltrim(StringDBColumn c){
		return c.ltrim();
	}
	
	/**
	 * LENGTHを返します。
	 * @param c 対象DBカラム
	 * @return LENGTH
	 */
	public static NumberDBColumn<Integer> length(StringDBColumn c){
		return c.length();
	}
	
	/**
	 * CONCATを返します。
	 * @param c 対象DBカラム
	 * @param s 結合対象カラム
	 * @return CONCAT
	 */
	public static StringDBColumn concat(StringDBColumn c,StringDBColumn s){
		return c.concat(s);
	}
	
	/**
	 * CONCATを返します。
	 * @param c 対象DBカラム
	 * @param s 結合文字列
	 * @return CONCAT
	 */
	public static StringDBColumn concat(StringDBColumn c,String s){
		return c.concat(s);
	}
	
	/**
	 * TO_NUMBERを返します。
	 * @param c 対象DBカラム
	 * @return TO_NUMBER
	 */
	public static NumberDBColumn<Long> toNumber(StringDBColumn c){
		return c.toNumber();
	}
	
	/**
	 * TO_NUMBERを返します。
	 * @param c 対象DBカラム
	 * @param format フォーマット
	 * @return TO_NUMBER
	 */
	public static NumberDBColumn<Long> toNumber(StringDBColumn c,String format){
		return c.toNumber(format);
	}
	
	/**
	 * TO_NUMBERを返します。
	 * @param c 対象DBカラム
	 * @param cls 型
	 * @param <T> DBカラムの型
	 * @return TO_NUMBER
	 */
	public static <T extends Number & Comparable<? super T>> NumberDBColumn<T> toNumber(StringDBColumn c,Class<T> cls){
		return c.toNumber(cls);
	}
	
	/**
	 * TO_NUMBERを返します。
	 * @param c 対象DBカラム
	 * @param cls 型
	 * @param format フォーマット
	 * @param <T> DBカラムの型
	 * @return TO_NUMBER
	 */
	public static <T extends Number & Comparable<? super T>> NumberDBColumn<T> toNumber(StringDBColumn c,Class<T> cls,String format){
		return c.toNumber(cls,format);
	}
	
	/**
	 * SUBSTRを返します。
	 * @param c 対象DBカラム
	 * @param from 開始位置
	 * @return SUBSTR
	 */
	public static StringDBColumn substr(StringDBColumn c,int from){
		return c.substring(from);
	}
	
	/**
	 * SUBSTRを返します。
	 * @param c 対象DBカラム
	 * @param from 開始位置
	 * @param to 終了位置
	 * @return SUBSTR
	 */
	public static StringDBColumn substr(StringDBColumn c,int from,int to){
		return c.substring(from, to);
	}
	
	/**
	 * SUBSTRを返します。
	 * @param c 対象DBカラム
	 * @param from 開始位置
	 * @return SUBSTR
	 */
	public static StringDBColumn substr(StringDBColumn c,NumberDBColumn<Integer> from){
		return c.substring(from);
	}
	
	/**
	 * SUBSTRを返します。
	 * @param c 対象DBカラム
	 * @param from 開始位置
	 * @param to 終了位置
	 * @return SUBSTR
	 */
	public static StringDBColumn substr(StringDBColumn c,NumberDBColumn<Integer> from,NumberDBColumn<Integer> to){
		return c.substring(from, to);
	}

	/**
	 * ANYを返します。
	 * @param c 対象DBカラム
	 * @return ANY
	 */
	public static <V extends Comparable<? super V>> ComparableDBColumn<V> any(ComparableDBColumn<V> c){
		return c.any();
	}

	/**
	 * SOMEを返します。
	 * @param c 対象DBカラム
	 * @return SOME
	 */
	public static <V extends Comparable<? super V>> ComparableDBColumn<V> some(ComparableDBColumn<V> c){
		return c.some();
	}

	/**
	 * ALLを返します。
	 * @param c 対象DBカラム
	 * @return ALL
	 */
	public static <V extends Comparable<? super V>> ComparableDBColumn<V> all(ComparableDBColumn<V> c){
		return c.all();
	}

	/**
	 * UPDATE文のset句を返します。
	 * @param eqExps セットする値
	 * @return SET
	 */
	public static Set<Exp> set(Exp...eqExps){
		if(eqExps == null){
			return new HashSet<Exp>();
		}
		Set<Exp> set = new LinkedHashSet<Exp>();
		Collections.addAll(set, eqExps);
		return set;
	}
	
	/**
	 * INSERT文のinto句を返します。
	 * @param columns 列のリスト
	 * @return INTO
	 */
	public static Into into(DBColumn<?>... columns){
		return new Into(columns);
	}

	/**
	 * 現在日を取得するCURRENT_DATE句
	 */
	public static final DateDBColumnImpl<Date> CURRENT_DATE_SQL = new DateDBColumnImpl<Date>(null, "CURRENT_DATE");
	
	/**
	 * 現在日時を取得するCURRENT_TIMESTAMP句
	 */
	public static final DateDBColumnImpl<Timestamp> CURRENT_TIMESTAMP_SQL = new DateDBColumnImpl<Timestamp>(null, "CURRENT_TIMESTAMP");
	
	/**
	 * 現在時刻を取得するCURRENT_TIME句
	 */
	public static final DateDBColumnImpl<Time> CURRENT_TIME_SQL = new DateDBColumnImpl<Time>(null, "CURRENT_TIME");
	
	/**
	 * 現在日を取得するCURRENT_DATE句
	 */
	public static final DateDBColumnImpl<LocalDate> CURRENT_DATE = new DateDBColumnImpl<LocalDate>(null, "CURRENT_DATE");
	
	/**
	 * 現在日時を取得するCURRENT_TIMESTAMP句
	 */
	public static final DateDBColumnImpl<LocalDateTime> CURRENT_TIMESTAMP = new DateDBColumnImpl<LocalDateTime>(null, "CURRENT_TIMESTAMP");
	
	/**
	 * 現在時刻を取得するCURRENT_TIME句
	 */
	public static final DateDBColumnImpl<LocalTime> CURRENT_TIME = new DateDBColumnImpl<LocalTime>(null, "CURRENT_TIME");
	
	/**
	 * 日付加減の単位(年)
	 */
	public static final IntervalUnit YEAR = IntervalUnit.YEAR;

	/**
	 * 日付加減の単位(四半期)
	 */
	public static final IntervalUnit QUARTER = IntervalUnit.QUARTER;

	/**
	 * 日付加減の単位(月)
	 */
	public static final IntervalUnit MONTH = IntervalUnit.MONTH;

	/**
	 * 日付加減の単位(週)
	 */
	public static final IntervalUnit WEEK = IntervalUnit.WEEK;

	/**
	 * 日付加減の単位(日)
	 */
	public static final IntervalUnit DAY = IntervalUnit.DAY;

	/**
	 * 日付加減の単位(時間)
	 */
	public static final IntervalUnit HOUR = IntervalUnit.HOUR;

	/**
	 * 日付加減の単位(分)
	 */
	public static final IntervalUnit MINUTE = IntervalUnit.MINUTE;

	/**
	 * 日付加減の単位(秒)
	 */
	public static final IntervalUnit SECOND = IntervalUnit.SECOND;

	/**
	 * 日付加減の単位(ミリ秒)
	 */
	public static final IntervalUnit MICROSECOND = IntervalUnit.MICROSECOND;
}
