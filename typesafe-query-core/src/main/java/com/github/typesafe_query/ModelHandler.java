package com.github.typesafe_query;

import static com.github.typesafe_query.Q.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.typesafe_query.meta.IDBTable;
import com.github.typesafe_query.query.QueryExecutor;
import com.github.typesafe_query.query.QueryException;
import com.github.typesafe_query.query.internal.QueryUtils;
import com.github.typesafe_query.util.ClassUtils;
import com.github.typesafe_query.util.Tuple;

/**
 * TODO v0.x.x バッチ用インスタンスを作成するファクトリメソッドを追加する？ #23
 * @author Takahiko Sato(MOSA architect Inc.)
 */
public class ModelHandler<T>{
	
	private static Logger logger = LoggerFactory.getLogger(ModelHandler.class);
	
	private static final String SQL_INSERT = "INSERT INTO %s (%s) VALUES (%s)";
	private static final String SQL_UPDATE = "UPDATE %s SET %s WHERE %s";
	private static final String SQL_DELETE = "DELETE FROM %s WHERE %s";
	
	private Class<T> modelClass;
	private IDBTable root;
	private ModelDescription modelDescription;
	
	public ModelHandler(Class<T> modelClass,IDBTable table,ModelDescription description) {
		this.modelClass = modelClass;
		this.root = table;
		this.modelDescription = description;
	}

	/**
	 * このModelを作成します。
	 * @param model モデル
	 * @return 作成したキー
	 */
	public Long createByGeneratedKey(T model){
		if(model == null){
			throw new NullPointerException("対象オブジェクトがnullです");
		}
		
		List<String> sets = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		List<Object> params = new ArrayList<Object>();
		
		List<Tuple<String, String>> all = modelDescription.getValueNames();
		for(Tuple<String, String> t : all){
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
		for(Object o : params){
			q.addParam(o);
		}
		
		Long r = q.insert();
		
		//生成キーがー複数のことはありえる？
		List<Tuple<String, String>> ids = modelDescription.getIdNames();
		if(ids.size() > 1){
			throw new QueryException("複合キーは自動生成に対応していません");
		}
		Tuple<String, String> id = ids.get(0);
		Field f = ClassUtils.getField(id._2, modelClass);
		ClassUtils.callSetter(f, Long.class, modelClass, r);
		
		return r;
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
	public void create(T model){
		if(model == null){
			throw new NullPointerException("対象オブジェクトがnullです");
		}
		
		List<String> sets = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		List<Object> params = new ArrayList<Object>();
		
		List<Tuple<String, String>> all = modelDescription.getAllNames();
		for(Tuple<String, String> t : all){
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
			params.add(convertParamType(f, targetModel));
		}
		
		String sql = String.format(SQL_INSERT,root.getName() ,QueryUtils.joinWith(",", sets),QueryUtils.joinWith(",", values));
		
		QueryExecutor q = createExecutor(sql);
		for(Object o : params){
			q.addParam(o);
		}
		
		q.executeUpdate();
	}
	
	/**
	 * このModelを更新します。
	 * TODO v0.3.x 一部だけ更新を追加したい #31
	 * @param model モデル
	 */
	public void save(T model){
		if(model == null){
			throw new NullPointerException("対象オブジェクトがnullです");
		}
		
		List<String> sets = new ArrayList<String>();
		List<Object> params = new ArrayList<Object>();
		
		List<Tuple<String, String>> values = modelDescription.getValueNames();
		for(Tuple<String, String> t : values){
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
				params.add(convertParamType(f, targetModel));
			}else{
				Field f = ClassUtils.getField(t._2, modelClass);
				if(f == null){
					throw new RuntimeException(String.format("%sクラスのフィールド%sが見つかりません", modelClass.getSimpleName(),t._2));
				}
				sets.add(t._1 + "=?");
				params.add(convertParamType(f, model));
			}
		}
		
		List<String> where = new ArrayList<String>();
		List<Tuple<String, String>> ids = modelDescription.getIdNames();
		for(Tuple<String, String> t : ids){
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
			params.add(key);
		}
		
		String sql = String.format(SQL_UPDATE,root.getName() ,QueryUtils.joinWith(",", sets), QueryUtils.joinWith(" and ", where));
		
		QueryExecutor q = createExecutor(sql);
		for(Object o : params){
			q.addParam(o);
		}
		
		q.executeUpdate();
	}
	
	/**
	 * このModelを削除します。
	 * @param model モデル
	 */
	public void delete(T model){
		if(model == null){
			throw new NullPointerException("対象オブジェクトがnullです");
		}
		
		List<Object> params = new ArrayList<Object>();
		List<String> where = new ArrayList<String>();
		List<Tuple<String, String>> ids = modelDescription.getIdNames();
		for(Tuple<String, String> t : ids){
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
			params.add(key);
		}
		
		String sql = String.format(SQL_DELETE,root.getName(),QueryUtils.joinWith(" and ", where));
		
		QueryExecutor q = createExecutor(sql);
		for(Object o : params){
			q.addParam(o);
		}
		
		q.executeUpdate();
	}
	
	protected QueryExecutor createExecutor(String sql){
		return stringQuery(sql).forOnce();
	}
}
