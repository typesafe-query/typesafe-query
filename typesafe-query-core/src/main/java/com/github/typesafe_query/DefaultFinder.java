package com.github.typesafe_query;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.github.typesafe_query.annotation.Converter;
import com.github.typesafe_query.jdbc.convert.TypeConverter;
import com.github.typesafe_query.meta.DBTable;
import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.Order;
import com.github.typesafe_query.query.QueryExecutor;
import com.github.typesafe_query.query.SQLQuery;
import com.github.typesafe_query.query.internal.DefaultQueryContext;
import com.github.typesafe_query.query.internal.QueryUtils;
import com.github.typesafe_query.query.internal.SimpleQueryExecutor;
import com.github.typesafe_query.util.ClassUtils;
import com.github.typesafe_query.util.Pair;

/**
 * モデルの検索を行います。
 * <p>検索を行うクラスです</p>
 * 
 * TODO v0.3.x 再利用可能インスタンスを作成するファクトリメソッドを追加する？ #23
 * TODO 同じようなコードが多いのでリファクタリングする
 * @author Takahiko Sato(MOSA architect Inc.)
 */
public class DefaultFinder<I,T> implements Finder<I, T>{
	
	private static final String SQL_TEMPLATE_COUNT = "SELECT COUNT(1) AS CNT FROM %s";
	private static final String SQL_TEMPLATE_BY_ID = "SELECT * FROM %s WHERE %s";
	
	private Class<T> modelClass;
	
	private DBTable root;
	
	private ModelDescription<T> modelDescription;
	
	private DefaultFinder<I,T> finder;
	
	private Boolean includeDefault = true;
	
	public DefaultFinder(DefaultFinder<I,T> finder) {
		this(finder.modelDescription);
		this.finder = finder;
		this.includeDefault = false;
	}
	
	/**
	 * モデルクラス、テーブル、モデル詳細を指定して新しいインスタンスを生成します。
	 * @param modelDescription モデル詳細
	 */
	public DefaultFinder(ModelDescription<T> modelDescription) {
		this.modelClass = modelDescription.getModelClass();
		this.root = modelDescription.getTable();
		this.modelDescription = modelDescription;
	}
	
	@Override
	public Finder<I,T> includeDefault(){
		return this.finder;
	}
	
	@Override
	public T requiredById(I id){
		return byId(id).orElseThrow(()->new RuntimeException("Record is required but no result."));
	}
	
	/**
	 * プライマリーキーで検索します
	 * @param id プライマリーキー
	 * @return Model
	 */
	@Override
	public Optional<T> byId(I id){
		if(id == null){
			throw new NullPointerException("IDがnullです");
		}
		
		List<Pair<Object,TypeConverter>> params = new ArrayList<>();
		List<String> where = new ArrayList<>();
		
		List<Pair<String, String>> ids = modelDescription.getIdNames();
		for(Pair<String, String> t : ids){
			if(t._2.contains("/")){
				String[] subNames = t._2.split("/");
				if(subNames.length != 2){
					throw new RuntimeException(String.format("フィールド名%sが不正です", t._2));
				}
				Field embField = ClassUtils.getField(subNames[0], modelClass);
				if(embField == null){
					throw new RuntimeException(String.format("%sクラスのフィールド%sが見つかりません", modelClass.getSimpleName(),subNames[0]));
				}
				Class<?> emb = embField.getType();
				Field f = ClassUtils.getField(subNames[1], emb);
				if(f.isAnnotationPresent(Converter.class)){
					Converter c = f.getAnnotation(Converter.class);
					params.add(new Pair<>(ClassUtils.callGetter(f, id),ClassUtils.newInstance(c.value())));
				}else{
					params.add(new Pair<>(ClassUtils.callGetter(f, id),null));
				}
			}else{
				Field f = ClassUtils.getField(t._2, modelClass);
				if(f == null){
					throw new RuntimeException(String.format("%sクラスのフィールド%sが見つかりません", modelClass.getSimpleName(),t._2));
				}
				params.add(new Pair<>(id,null));
			}
			
			where.add(t._1 + "=?");
		}
		
		String sql = String.format(SQL_TEMPLATE_BY_ID,root.getName() ,QueryUtils.joinWith(" and ", where));
		Exp[] exps = getExps();
		if(exps != null && exps.length > 0){
			DefaultQueryContext context = new DefaultQueryContext(root);
			sql += " and " + Q.and(exps).getSQL(context);
		}
		
		QueryExecutor q = Q.stringQuery(sql).forOnce();
		params.forEach(p -> q.addParam(p._1,p._2));
		
		return q.getResult(modelClass);
	}
	
	/**
	 * 件数を返します。
	 * @return 件数
	 */
	@Override
	public long count(){
		Exp[] exps = getExps();
		String where = null;
		if(exps != null && exps.length > 0){
			DefaultQueryContext context = new DefaultQueryContext(root);
			where = Q.and(exps).getSQL(context);
		}
		String w = where != null && !where.isEmpty() ? String.format(" WHERE %s", where) : "";
		Optional<Object> count = Q.stringQuery(String.format(SQL_TEMPLATE_COUNT, root.getName()) + w)
			.forOnce()
			.getResult((rd) -> rd.get("CNT"));
		//JDBCによってcountの戻りの型が違う
		return ((Number)count.get()).longValue();
	}

	/**
	 * 条件を指定して件数を返します。
	 * @param expressions 検索条件
	 * @return 件数
	 */
	@Override
	public long countWhere(Exp... expressions){
		if(expressions == null || expressions.length == 0){
			return count();
		}
		String sql = String.format(SQL_TEMPLATE_COUNT, root.getName() + " " + root.getAlias());
		
		Exp exp;
		if(expressions.length == 1){
			exp = expressions[0];
		}else{
			exp = Q.and(expressions);
		}
		exp = Q.and(getExps(exp));
		DefaultQueryContext context = new DefaultQueryContext(root);
		String p = exp.getSQL(context);
		if(p != null && !p.isEmpty()){
			sql = sql + " WHERE " + p;
		}
		Optional<Object> count = Q.stringQuery(sql)
				.forOnce()
				.getResult((rd) -> rd.get("CNT"));
		return ((Number)count.get()).longValue();
	}

	/**
	 * 一覧を返します
	 * @return 一覧
	 */
	@Override
	public List<T> list(Order...orders){
		return Q.select().from(root).where(getExps()).orderBy(orders).forOnce().getResultList(modelClass);
	}
	
	/**
	 * 件数を指定して一覧を返します。
	 * @param limit 最大件数
	 * @return 一覧
	 */
	@Override
	public List<T> list(int limit,Order...orders){
		if(limit < 1){
			throw new IllegalArgumentException("limit must be greater than 0");
		}
		return Q.select()
			.from(root)
			.where(getExps())
			.orderBy(orders)
			.limit(limit)
			.forOnce()
			.getResultList(modelClass);
	}
	
	/**
	 * 開始位置と件数を指定して一覧を返します。
	 * @param offset 開始位置
	 * @param limit 最大件数
	 * @return 一覧
	 */
	@Override
	public List<T> list(int offset,int limit,Order...orders){
		if(limit < 1){
			throw new IllegalArgumentException("limit must be greater than 0");
		}
		
		if(offset < 0){
			throw new IllegalArgumentException("offset must be greater than -1");
		}
		
		return Q.select()
				.from(root)
				.where(getExps())
				.limit(limit)
				.orderBy(orders)
				.offset(offset)
				.forOnce()
				.getResultList(modelClass);
	}
	
	@Override
	public T requiredWhere(Exp... expressions){
		return where(expressions).orElseThrow(() -> new RuntimeException("Record is required but no result."));
	}
	
	/**
	 * 条件を指定して1件取得します
	 * @param expressions 条件
	 * @return 1件の結果
	 */
	@Override
	public Optional<T> where(Exp... expressions){
		return Q.select()
				.from(root)
				.where(getExps(expressions))
				.forOnce()
				.getResult(modelClass);
	}
	
	/**
	 * 条件を指定して一覧を返します。
	 * @param expression 条件
	 * @return 一覧
	 */
	@Override
	public List<T> listWhere(Exp expression,Order...orders){
		return Q.select()
				.from(root)
				.where(getExps(expression))
				.orderBy(orders)
				.forOnce()
				.getResultList(modelClass);
	}
	
	/**
	 * 条件、件数を指定して一覧を返します。
	 * @param expression 条件
	 * @param limit 最大件数
	 * @return 一覧
	 */
	@Override
	public List<T> listWhere(Exp expression,Integer limit,Order...orders){
		if(limit < 1){
			throw new IllegalArgumentException("limit must be greater than 0");
		}
		return Q.select()
				.from(root)
				.where(getExps(expression))
				.orderBy(orders)
				.limit(limit)
				.forOnce()
				.getResultList(modelClass);
	}
	
	/**
	 * 条件、開始位置、件数を指定して一覧を返します。
	 * @param expression 条件
	 * @param offset 開始位置
	 * @param limit 件数
	 * @return 一覧
	 */
	@Override
	public List<T> listWhere(Exp expression,Integer offset,Integer limit,Order...orders){
		if(limit < 1){
			throw new IllegalArgumentException("limit must be greater than 0");
		}
		
		if(offset < 0){
			throw new IllegalArgumentException("offset must be greater than -1");
		}
		return Q.select()
				.from(root)
				.where(getExps(expression))
				.orderBy(orders)
				.limit(limit)
				.offset(offset)
				.forOnce()
				.getResultList(modelClass);
	}

	@Override
	public void fetch(Predicate<T> p, Order... orders) {
		Q.select().from(root).where(getExps()).orderBy(orders).forOnce().fetch(modelClass, p);
	}

	@Override
	public void fetch(Predicate<T> p, int limit, Order... orders) {
		if(limit < 1){
			throw new IllegalArgumentException("limit must be greater than 0");
		}
		Q.select()
			.from(root)
			.where(getExps())
			.orderBy(orders)
			.limit(limit)
			.forOnce()
			.fetch(modelClass, p);
	}

	@Override
	public void fetch(Predicate<T> p, int offset, int limit, Order... orders) {
		if(limit < 1){
			throw new IllegalArgumentException("limit must be greater than 0");
		}
		
		if(offset < 0){
			throw new IllegalArgumentException("offset must be greater than -1");
		}
		
		Q.select()
			.from(root)
			.where(getExps())
			.limit(limit)
			.orderBy(orders)
			.offset(offset)
			.forOnce()
			.fetch(modelClass, p);
	}

	@Override
	public void fetch(Consumer<T> p, Order... orders) {
		Q.select().from(root).where(getExps()).orderBy(orders).forOnce().fetch(modelClass, p);
	}

	@Override
	public void fetch(Consumer<T> p, int limit, Order... orders) {
		if(limit < 1){
			throw new IllegalArgumentException("limit must be greater than 0");
		}
		Q.select()
			.from(root)
			.where(getExps())
			.orderBy(orders)
			.limit(limit)
			.forOnce()
			.fetch(modelClass, p);
	}

	@Override
	public void fetch(Consumer<T> p, int offset, int limit, Order... orders) {
		if(limit < 1){
			throw new IllegalArgumentException("limit must be greater than 0");
		}
		
		if(offset < 0){
			throw new IllegalArgumentException("offset must be greater than -1");
		}
		
		Q.select()
			.from(root)
			.where(getExps())
			.limit(limit)
			.orderBy(orders)
			.offset(offset)
			.forOnce()
			.fetch(modelClass, p);
	}

	@Override
	public void fetchWhere(Exp expression, Predicate<T> p, Order... orders) {
		Q.select()
			.from(root)
			.where(getExps(expression))
			.orderBy(orders)
			.forOnce()
			.fetch(modelClass, p);
	}

	@Override
	public void fetchWhere(Exp expression, Predicate<T> p, Integer limit, Order... orders) {
		if(limit < 1){
			throw new IllegalArgumentException("limit must be greater than 0");
		}
		Q.select()
			.from(root)
			.where(getExps(expression))
			.orderBy(orders)
			.limit(limit)
			.forOnce()
			.fetch(modelClass, p);
	}

	@Override
	public void fetchWhere(Exp expression, Predicate<T> p, Integer offset, Integer limit, Order... orders) {
		if(limit < 1){
			throw new IllegalArgumentException("limit must be greater than 0");
		}
		
		if(offset < 0){
			throw new IllegalArgumentException("offset must be greater than -1");
		}
		Q.select()
			.from(root)
			.where(getExps(expression))
			.orderBy(orders)
			.limit(limit)
			.offset(offset)
			.forOnce()
			.fetch(modelClass, p);
	}

	@Override
	public void fetchWhere(Exp expression, Consumer<T> p, Order... orders) {
		Q.select()
			.from(root)
			.where(getExps(expression))
			.orderBy(orders)
			.forOnce()
			.fetch(modelClass, p);
	}

	@Override
	public void fetchWhere(Exp expression, Consumer<T> p, Integer limit, Order... orders) {
		if(limit < 1){
			throw new IllegalArgumentException("limit must be greater than 0");
		}
		Q.select()
			.from(root)
			.where(getExps(expression))
			.orderBy(orders)
			.limit(limit)
			.forOnce()
			.fetch(modelClass, p);
	}

	@Override
	public void fetchWhere(Exp expression, Consumer<T> p, Integer offset, Integer limit, Order... orders) {
		if(limit < 1){
			throw new IllegalArgumentException("limit must be greater than 0");
		}
		
		if(offset < 0){
			throw new IllegalArgumentException("offset must be greater than -1");
		}
		Q.select()
			.from(root)
			.where(getExps(expression))
			.orderBy(orders)
			.limit(limit)
			.offset(offset)
			.forOnce()
			.fetch(modelClass, p);
	}
	protected QueryExecutor createExecutor(SQLQuery sqlQuery){
		return new SimpleQueryExecutor(sqlQuery);
	}
	
	private Exp[] getExps(Exp... expressions){
		if(!this.includeDefault) {
			return expressions;
		}
		
		List<Exp> defaultExps = modelDescription.getDefaultExps();
		
		if(defaultExps == null) {
			return expressions;
		}
		if(defaultExps.isEmpty()) {
			return expressions;
		}
		
		defaultExps.addAll(Arrays.asList(expressions));
		Exp[] exps = new Exp[defaultExps.size()];
		defaultExps.toArray(exps);
		return exps;
	}
}
