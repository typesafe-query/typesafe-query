package com.github.typesafe_query;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.typesafe_query.annotation.Converter;
import com.github.typesafe_query.jdbc.convert.TypeConverter;
import com.github.typesafe_query.meta.DBColumn;
import com.github.typesafe_query.meta.DBTable;
import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.InvalidQueryException;
import com.github.typesafe_query.query.QueryException;
import com.github.typesafe_query.query.QueryExecutor;
import com.github.typesafe_query.query.internal.DefaultQueryContext;
import com.github.typesafe_query.query.internal.QueryUtils;
import com.github.typesafe_query.query.internal.SimpleQueryExecutor;
import com.github.typesafe_query.util.ClassUtils;
import com.github.typesafe_query.util.Pair;

/**
 * TODO v0.x.x バッチ用インスタンスを作成するファクトリメソッドを追加する？ #23
 * @author Takahiko Sato(MOSA architect Inc.)
 */
public class DefaultModelHandler<T> implements ModelHandler<T>{
	
	private static Logger logger = LoggerFactory.getLogger(DefaultModelHandler.class);
	
	private static final String SQL_INSERT = "INSERT INTO %s (%s) VALUES (%s)";
	private static final String SQL_UPDATE = "UPDATE %s SET %s WHERE %s";
	private static final String SQL_DELETE = "DELETE FROM %s WHERE %s";
	
	private Class<T> modelClass;
	private DBTable root;
	private ModelDescription<T> modelDescription;
	
	public DefaultModelHandler(ModelDescription<T> description) {
		this.modelClass = description.getModelClass();
		this.root = description.getTable();
		this.modelDescription = description;
	}

	/**
	 * このModelを作成します。
	 * @param model モデル
	 * @return 作成したキー
	 */
	@Deprecated
	public Long createByGeneratedKey(T model){
		if(model == null){
			throw new NullPointerException("対象オブジェクトがnullです");
		}
		
		List<String> sets = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		List<Object> params = new ArrayList<Object>();
		
		List<Pair<String, String>> all = modelDescription.getValueNames();
		for(Pair<String, String> t : all){
			Field f;
			Object targetModel;
			if(t._2.contains("/")){
				String[] subNames = t._2.split("/");
				if(subNames.length != 2){
					throw new RuntimeException(String.format("フィールド名%sが不正です", t._2));
				}
				f = ClassUtils.getField(subNames[0], modelClass);
				if(f == null){
					throw new RuntimeException(String.format("%sクラスのフィールド%sが見つかりません", modelClass.getSimpleName(),subNames[0]));
				}
				targetModel = ClassUtils.callGetter(f, model);
				if(targetModel == null){
					//targetModelがnullの場合、インスタンスを生成してあげる
					logger.debug("Creating new instance. targetModel is null.");
					targetModel = ClassUtils.newInstance(f.getType());
				}
				Class<?> emb = f.getType();
				f = ClassUtils.getField(subNames[1], emb);
			}else{
				f = ClassUtils.getField(t._2, modelClass);
				targetModel = model;
			}
			
			if(f == null){
				throw new RuntimeException(String.format("%sクラスのフィールド%sが見つかりません", modelClass.getSimpleName(),t._2));
			}
			sets.add(t._1);
			values.add("?");
			params.add(convertParamType(f, targetModel));
		}
		
		String sql = String.format(SQL_INSERT,root.getName() ,QueryUtils.joinWith(",", sets),QueryUtils.joinWith(",", values));
		
		QueryExecutor q = createExecutor(sql);
		//TODO converter対応 どうせ消しちゃうメソッドだから無視
		for(Object o : params){
			q.addParam(o);
		}
		
		Long r = q.insert();
		
		//生成キーがー複数のことはありえる？
		List<Pair<String, String>> ids = modelDescription.getIdNames();
		if(ids.size() > 1){
			throw new QueryException("複合キーは自動生成に対応していません");
		}
		Pair<String, String> id = ids.get(0);
		Field f = ClassUtils.getField(id._2, modelClass);
		ClassUtils.callSetter(f, Long.class, modelClass, r);
		
		return r;
	}
	
	private boolean createWithGeneratedKey(T model){
		if(model == null){
			throw new NullPointerException("対象オブジェクトがnullです");
		}
		
		List<String> sets = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		List<Pair<Object, TypeConverter>> params = new ArrayList<>();
		
		List<Pair<String, String>> all = modelDescription.getValueNames();
		for(Pair<String, String> t : all){
			Field f;
			Object targetModel;
			if(t._2.contains("/")){
				String[] subNames = t._2.split("/");
				if(subNames.length != 2){
					throw new RuntimeException(String.format("フィールド名%sが不正です", t._2));
				}
				f = ClassUtils.getField(subNames[0], modelClass);
				if(f == null){
					throw new RuntimeException(String.format("%sクラスのフィールド%sが見つかりません", modelClass.getSimpleName(),subNames[0]));
				}
				targetModel = ClassUtils.callGetter(f, model);
				if(targetModel == null){
					//targetModelがnullの場合、インスタンスを生成してあげる
					logger.debug("Creating new instance. targetModel is null.");
					targetModel = ClassUtils.newInstance(f.getType());
				}
				Class<?> emb = f.getType();
				f = ClassUtils.getField(subNames[1], emb);
			}else{
				f = ClassUtils.getField(t._2, modelClass);
				targetModel = model;
			}
			
			if(f == null){
				throw new RuntimeException(String.format("%sクラスのフィールド%sが見つかりません", modelClass.getSimpleName(),t._2));
			}
			sets.add(t._1);
			values.add("?");
			if(f.isAnnotationPresent(Converter.class)){
				Converter c = f.getAnnotation(Converter.class);
				params.add(new Pair<>(ClassUtils.callGetter(f, targetModel),ClassUtils.newInstance(c.value())));
			}else{
				params.add(new Pair<>(ClassUtils.callGetter(f, targetModel),null));
			}
		}
		
		String sql = String.format(SQL_INSERT,root.getName() ,QueryUtils.joinWith(",", sets),QueryUtils.joinWith(",", values));
		
		QueryExecutor q = createExecutor(sql);
		params.forEach(p -> q.addParam(p._1, p._2));
		
		try {
			Long r = q.insert();
			
			//生成キーがー複数のことはありえる？
			List<Pair<String, String>> ids = modelDescription.getIdNames();
			if(ids.size() > 1){
				throw new QueryException("複合キーは自動生成に対応していません");
			}
			Pair<String, String> id = ids.get(0);
			Field f = ClassUtils.getField(id._2, modelClass);
			ClassUtils.callSetter(f, Long.class, model, r);
			
			return true;
		} catch (QueryException e) {
			logger.warn("Exception thrown!!",e);
			return false;
		}
	}
	
	private Object convertParamType(Field f,Object targetModel){
		//Optional判定はExecutableQuery#addParamでやるため不要
		Object o = ClassUtils.callGetter(f, targetModel);
		return o;
	}

	/**
	 * このModelを作成します。
	 * @param model モデル
	 */
	public boolean create(T model){
		if(model == null){
			throw new NullPointerException("対象オブジェクトがnullです");
		}
		
		if(modelDescription.isIdGenerated()){
			return createWithGeneratedKey(model);
		}
		
		List<String> sets = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		List<Pair<Object,TypeConverter>> params = new ArrayList<>();
		
		List<Pair<String, String>> all = modelDescription.getAllNames();
		for(Pair<String, String> t : all){
			Field f;
			Object targetModel;
			if(t._2.contains("/")){
				String[] subNames = t._2.split("/");
				if(subNames.length != 2){
					throw new RuntimeException(String.format("フィールド名%sが不正です", t._2));
				}
				f = ClassUtils.getField(subNames[0], modelClass);
				targetModel = ClassUtils.callGetter(f, model);
				if(f == null){
					throw new RuntimeException(String.format("%sクラスのフィールド%sが見つかりません", modelClass.getSimpleName(),subNames[0]));
				}
				Class<?> emb = f.getType();
				f = ClassUtils.getField(subNames[1], emb);
			}else{
				f = ClassUtils.getField(t._2, modelClass);
				targetModel = model;
			}
			
			if(f == null){
				throw new RuntimeException(String.format("%sクラスのフィールド%sが見つかりません", modelClass.getSimpleName(),t._2));
			}
			sets.add(t._1);
			values.add("?");
			if(f.isAnnotationPresent(Converter.class)){
				Converter c = f.getAnnotation(Converter.class);
				params.add(new Pair<>(ClassUtils.callGetter(f, targetModel),ClassUtils.newInstance(c.value())));
			}else{
				params.add(new Pair<>(ClassUtils.callGetter(f, targetModel),null));
			}
		}
		
		String sql = String.format(SQL_INSERT,root.getName() ,QueryUtils.joinWith(",", sets),QueryUtils.joinWith(",", values));
		
		QueryExecutor q = createExecutor(sql);
		params.forEach(p -> q.addParam(p._1,p._2));
		
		try {
			return q.executeUpdate() != 0;
		} catch (QueryException e) {
			logger.warn("Create failed.",e);
			return false;
		}
	}
	
	/**
	 * このModelを更新します。
	 * TODO v0.3.x 一部だけ更新を追加したい #31
	 * @param model モデル
	 */
	public boolean save(T model){
		if(model == null){
			throw new NullPointerException("対象オブジェクトがnullです");
		}
		
		List<String> sets = new ArrayList<String>();
		List<Pair<Object,TypeConverter>> params = new ArrayList<>();
		
		List<Pair<String, String>> values = modelDescription.getValueNames();
		for(Pair<String, String> t : values){
			if(t._2.contains("/")){
				String[] subNames = t._2.split("/");
				if(subNames.length != 2){
					throw new RuntimeException(String.format("フィールド名%sが不正です", t._2));
				}
				Field f = ClassUtils.getField(subNames[0], modelClass);
				Object targetModel = ClassUtils.callGetter(f, model);
				if(f == null){
					throw new RuntimeException(String.format("%sクラスのフィールド%sが見つかりません", modelClass.getSimpleName(),subNames[0]));
				}
				Class<?> emb = f.getType();
				f = ClassUtils.getField(subNames[1], emb);
				if(f == null){
					throw new RuntimeException(String.format("%sクラスのフィールド%sが見つかりません", modelClass.getSimpleName(),t._2));
				}
				sets.add(t._1 + "=?");
				if(f.isAnnotationPresent(Converter.class)){
					Converter c = f.getAnnotation(Converter.class);
					params.add(new Pair<>(ClassUtils.callGetter(f, targetModel),ClassUtils.newInstance(c.value())));
				}else{
					params.add(new Pair<>(ClassUtils.callGetter(f, targetModel),null));
				}
			}else{
				Field f = ClassUtils.getField(t._2, modelClass);
				if(f == null){
					throw new RuntimeException(String.format("%sクラスのフィールド%sが見つかりません", modelClass.getSimpleName(),t._2));
				}
				sets.add(t._1 + "=?");
				if(f.isAnnotationPresent(Converter.class)){
					Converter c = f.getAnnotation(Converter.class);
					params.add(new Pair<>(ClassUtils.callGetter(f, model),ClassUtils.newInstance(c.value())));
				}else{
					params.add(new Pair<>(ClassUtils.callGetter(f, model),null));
				}
			}
		}
		
		List<String> where = new ArrayList<String>();
		List<Pair<String, String>> ids = modelDescription.getIdNames();
		for(Pair<String, String> t : ids){
			Field f;
			Object targetModel;
			if(t._2.contains("/")){
				String[] subNames = t._2.split("/");
				if(subNames.length != 2){
					throw new RuntimeException(String.format("フィールド名%sが不正です", t._2));
				}
				f = ClassUtils.getField(subNames[0], modelClass);
				targetModel = ClassUtils.callGetter(f, model);
				if(f == null){
					throw new RuntimeException(String.format("%sクラスのフィールド%sが見つかりません", modelClass.getSimpleName(),subNames[0]));
				}
				Class<?> emb = f.getType();
				f = ClassUtils.getField(subNames[1], emb);
			}else{
				f = ClassUtils.getField(t._2, modelClass);
				targetModel = model;
			}
			
			if(f == null){
				throw new RuntimeException(String.format("%sクラスのフィールド%sが見つかりません", modelClass.getSimpleName(),t._2));
			}
			where.add(t._1 + "=?");
			
			Object key = convertParamType(f, targetModel);
			if(key == null){
				throw new QueryException("条件のプライマリーキー項目がnullです " + f.getName());
			}
			if(f.isAnnotationPresent(Converter.class)){
				Converter c = f.getAnnotation(Converter.class);
				params.add(new Pair<>(key,ClassUtils.newInstance(c.value())));
			}else{
				params.add(new Pair<>(key,null));
			}
		}
		
		String sql = String.format(SQL_UPDATE,root.getName() ,QueryUtils.joinWith(",", sets), QueryUtils.joinWith(" and ", where));
		
		QueryExecutor q = createExecutor(sql);
		params.forEach(p -> q.addParam(p._1,p._2));
		
		try {
			return q.executeUpdate() != 0;
		} catch (QueryException e) {
			logger.warn("Save failed.",e);
			return false;
		}
	}
	
	@Override
	public boolean save(T model, DBColumn<?>... columns) {
		if(model == null){
			throw new NullPointerException("対象オブジェクトがnullです");
		}
		
		if(columns == null ||
				columns.length == 0 ||
				Arrays.stream(columns).anyMatch(c -> "*".equals(c.getName()))){
			return save(model);
		}
		
		if(Arrays.stream(columns)
				.anyMatch(c -> !c.getTable().getSimpleName().equals(root.getSimpleName()))){
			throw new InvalidQueryException("Argument `columns` is not included by table " + root.getSimpleName());
		}
		
		Set<String> names = Arrays.stream(columns).map(c -> c.getName()).collect(Collectors.toSet());
		
		List<String> sets = new ArrayList<>();
		List<Pair<Object,TypeConverter>> params = new ArrayList<>();
		
		List<Pair<String, String>> values = modelDescription.getValueNames();
		for(Pair<String, String> t : values){
			if(!names.contains(t._1)){
				continue;
			}
			if(t._2.contains("/")){
				String[] subNames = t._2.split("/");
				if(subNames.length != 2){
					throw new RuntimeException(String.format("フィールド名%sが不正です", t._2));
				}
				Field f = ClassUtils.getField(subNames[0], modelClass);
				Object targetModel = ClassUtils.callGetter(f, model);
				if(f == null){
					throw new RuntimeException(String.format("%sクラスのフィールド%sが見つかりません", modelClass.getSimpleName(),subNames[0]));
				}
				Class<?> emb = f.getType();
				f = ClassUtils.getField(subNames[1], emb);
				if(f == null){
					throw new RuntimeException(String.format("%sクラスのフィールド%sが見つかりません", modelClass.getSimpleName(),t._2));
				}
				sets.add(t._1 + "=?");
				if(f.isAnnotationPresent(Converter.class)){
					Converter c = f.getAnnotation(Converter.class);
					params.add(new Pair<>(ClassUtils.callGetter(f, targetModel),ClassUtils.newInstance(c.value())));
				}else{
					params.add(new Pair<>(ClassUtils.callGetter(f, targetModel),null));
				}
			}else{
				Field f = ClassUtils.getField(t._2, modelClass);
				if(f == null){
					throw new RuntimeException(String.format("%sクラスのフィールド%sが見つかりません", modelClass.getSimpleName(),t._2));
				}
				sets.add(t._1 + "=?");
				if(f.isAnnotationPresent(Converter.class)){
					Converter c = f.getAnnotation(Converter.class);
					params.add(new Pair<>(ClassUtils.callGetter(f, model),ClassUtils.newInstance(c.value())));
				}else{
					params.add(new Pair<>(ClassUtils.callGetter(f, model),null));
				}
			}
		}
		
		if(sets.isEmpty()){
			throw new InvalidQueryException("Save target columns are empty.");
		}
		
		List<String> where = new ArrayList<String>();
		List<Pair<String, String>> ids = modelDescription.getIdNames();
		for(Pair<String, String> t : ids){
			Field f;
			Object targetModel;
			if(t._2.contains("/")){
				String[] subNames = t._2.split("/");
				if(subNames.length != 2){
					throw new RuntimeException(String.format("フィールド名%sが不正です", t._2));
				}
				f = ClassUtils.getField(subNames[0], modelClass);
				targetModel = ClassUtils.callGetter(f, model);
				if(f == null){
					throw new RuntimeException(String.format("%sクラスのフィールド%sが見つかりません", modelClass.getSimpleName(),subNames[0]));
				}
				Class<?> emb = f.getType();
				f = ClassUtils.getField(subNames[1], emb);
			}else{
				f = ClassUtils.getField(t._2, modelClass);
				targetModel = model;
			}
			
			if(f == null){
				throw new RuntimeException(String.format("%sクラスのフィールド%sが見つかりません", modelClass.getSimpleName(),t._2));
			}
			where.add(t._1 + "=?");
			
			Object key = convertParamType(f, targetModel);
			if(key == null){
				throw new QueryException("条件のプライマリーキー項目がnullです " + f.getName());
			}
			if(f.isAnnotationPresent(Converter.class)){
				Converter c = f.getAnnotation(Converter.class);
				params.add(new Pair<>(key,ClassUtils.newInstance(c.value())));
			}else{
				params.add(new Pair<>(key,null));
			}
		}
		
		String sql = String.format(SQL_UPDATE,root.getName() ,QueryUtils.joinWith(",", sets), QueryUtils.joinWith(" and ", where));
		
		QueryExecutor q = createExecutor(sql);
		params.forEach(p -> q.addParam(p._1,p._2));
		
		try {
			return q.executeUpdate() != 0;
		} catch (QueryException e) {
			logger.warn("Save failed.",e);
			return false;
		}
	}

	/**
	 * このModelを削除します。
	 * @param model モデル
	 */
	public boolean delete(T model){
		if(model == null){
			throw new NullPointerException("対象オブジェクトがnullです");
		}
		
		List<Pair<Object, TypeConverter>> params = new ArrayList<>();
		List<String> where = new ArrayList<String>();
		List<Pair<String, String>> ids = modelDescription.getIdNames();
		for(Pair<String, String> t : ids){
			Field f;
			Object targetModel;
			if(t._2.contains("/")){
				String[] subNames = t._2.split("/");
				if(subNames.length != 2){
					throw new RuntimeException(String.format("フィールド名%sが不正です", t._2));
				}
				f = ClassUtils.getField(subNames[0], modelClass);
				targetModel = ClassUtils.callGetter(f, model);
				if(f == null){
					throw new RuntimeException(String.format("%sクラスのフィールド%sが見つかりません", modelClass.getSimpleName(),subNames[0]));
				}
				Class<?> emb = f.getType();
				f = ClassUtils.getField(subNames[1], emb);
			}else{
				f = ClassUtils.getField(t._2, modelClass);
				targetModel = model;
			}
			
			if(f == null){
				throw new RuntimeException(String.format("%sクラスのフィールド%sが見つかりません", modelClass.getSimpleName(),t._2));
			}
			where.add(t._1 + "=?");
			Object key = convertParamType(f, targetModel);
			if(key == null){
				throw new QueryException("条件のプライマリーキー項目がnullです " + f.getName());
			}
			if(f.isAnnotationPresent(Converter.class)){
				Converter c = f.getAnnotation(Converter.class);
				params.add(new Pair<>(key,ClassUtils.newInstance(c.value())));
			}else{
				params.add(new Pair<>(key,null));
			}
		}
		
		String sql = String.format(SQL_DELETE,root.getName(),QueryUtils.joinWith(" and ", where));
		
		QueryExecutor q = createExecutor(sql);
		params.forEach(p -> q.addParam(p._1,p._2));
		
		try {
			return q.executeUpdate() != 0;
		} catch (QueryException e) {
			logger.warn("Delete failed.",e);
			return false;
		}
	}
	
	public boolean invalid(T model){
		if(model == null){
			throw new NullPointerException("対象オブジェクトがnullです");
		}
		List<Exp> invalidExps = modelDescription.getInvalidExps();
		if(invalidExps == null){
			throw new QueryException("@Invalidの指定がありません ");
		}
		if(invalidExps.isEmpty()){
			throw new QueryException("@Invalidの指定がありません ");
		}
		
		DefaultQueryContext context = new DefaultQueryContext(root);
		List<Pair<Object,TypeConverter>> params = new ArrayList<>();
		List<String> where = new ArrayList<String>();
		List<Pair<String, String>> ids = modelDescription.getIdNames();
		for(Pair<String, String> t : ids){
			Field f;
			Object targetModel;
			if(t._2.contains("/")){
				String[] subNames = t._2.split("/");
				if(subNames.length != 2){
					throw new RuntimeException(String.format("フィールド名%sが不正です", t._2));
				}
				f = ClassUtils.getField(subNames[0], modelClass);
				targetModel = ClassUtils.callGetter(f, model);
				if(f == null){
					throw new RuntimeException(String.format("%sクラスのフィールド%sが見つかりません", modelClass.getSimpleName(),subNames[0]));
				}
				Class<?> emb = f.getType();
				f = ClassUtils.getField(subNames[1], emb);
			}else{
				f = ClassUtils.getField(t._2, modelClass);
				targetModel = model;
			}
			
			if(f == null){
				throw new RuntimeException(String.format("%sクラスのフィールド%sが見つかりません", modelClass.getSimpleName(),t._2));
			}
			where.add(t._1 + "=?");
			
			Object key = convertParamType(f, targetModel);
			if(key == null){
				throw new QueryException("条件のプライマリーキー項目がnullです " + f.getName());
			}
			if(f.isAnnotationPresent(Converter.class)){
				Converter c = f.getAnnotation(Converter.class);
				params.add(new Pair<>(key,ClassUtils.newInstance(c.value())));
			}else{
				params.add(new Pair<>(key,null));
			}
		}
		
		// TODO 苦肉の策で '<>' → '='
		List<String> sets = invalidExps.stream().map(e -> e.getSQL(context).replace("<>", "=")).collect(Collectors.toList());
		String sql = String.format(SQL_UPDATE,root.getName() , String.join(", ", sets), QueryUtils.joinWith(" and ", where));
		
		QueryExecutor q = createExecutor(sql);
		params.forEach(p -> q.addParam(p._1,p._2));
		
		try {
			return q.executeUpdate() != 0;
		} catch (QueryException e) {
			logger.warn("Save failed.",e);
			return false;
		}
	}
	
	protected QueryExecutor createExecutor(String sql){
		return new SimpleQueryExecutor(Q.stringQuery(sql));
	}
}
