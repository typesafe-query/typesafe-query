package com.github.typesafe_query.jdbc.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.typesafe_query.DBManager;
import com.github.typesafe_query.annotation.Column;
import com.github.typesafe_query.annotation.Embedded;
import com.github.typesafe_query.annotation.EmbeddedId;
import com.github.typesafe_query.annotation.Transient;
import com.github.typesafe_query.query.QueryException;
import com.github.typesafe_query.util.ClassUtils;


/**
 * TODO v0.3.x パフォーマンスが悪いと思うので要検討 #33
 * TODO v0.3.x 2階層しか対応していないので無限階層に対応する。LazyLoadを見据えて。IDはどの階層まで許す？ #5
 * 
 * <p>NAME→name</p>
 * <p>USER_NAME→userName</p>
 * <p>USER_NAME→@Column(name="USER_NAME") uName</p>
 * 
 * @author Takahiko Sato(MOSA Architect Inc.)
 *
 */
public class BeanResultMapper<R> implements ResultMapper<R>{
	
	private static Logger logger = LoggerFactory.getLogger(BeanResultMapper.class);
	
	private Class<R> type;
	
	public BeanResultMapper(Class<R> type) {
		this.type = type;
	}

	@Override
	public R map(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		List<MappingMetadata> pairs = createColmunProperyPair(meta, type);
		
		R model = ClassUtils.newInstance(type);
		for(MappingMetadata t : pairs){
			if(t.propertyPath.contains("/")){
				String[] names = t.propertyPath.split("/");
				Field f = ClassUtils.getField(names[0], type);
				Object pk = ClassUtils.callGetter(f, model);
				if(pk == null){
					pk = ClassUtils.newInstance(f.getType());
					callSetter(f, model, pk);
				}
				
				f = ClassUtils.getField(names[1], f.getType());
				Object o = processColumn(rs, t.columnIndex, t.columnJavaType, f);
				callSetter(f, pk, o);
			}else{
				Field f = ClassUtils.getField(t.propertyPath, type);
				Object o = processColumn(rs, t.columnIndex, t.columnJavaType, f);
				callSetter(f, model, o);
			}
		}
		
		return model;
	}
	
	protected Object processColumn(ResultSet rs,int index,Class<?> columnJavaType,Field f) throws SQLException{
		return DBManager.getDialectTranslator().getValue(rs,index,columnJavaType,f);
	}

	private List<MappingMetadata> createColmunProperyPair(ResultSetMetaData meta,Class<?> type) throws SQLException{
		
		List<MappingMetadata> result = new ArrayList<>();
		
		Field[] fileds = ClassUtils.getAllFields(type);
		
		for(int i = 1; i <= meta.getColumnCount();i++){
			String colName = meta.getColumnLabel(i);
			
			//まずフィールド名にマッチするものかColumnアノテーションにマッチするものをさがす
			boolean found = false;
			for(Field f : fileds){
				if(isIgnoreField(f)){
					continue;
				}
				String propertyName = f.getName();
				if (equalsColumnAnnotation(colName, propertyName,type) ||
						equalsColumnProperty(colName, propertyName)) {
					if(!containsProperty(result, propertyName)){
						result.add(new MappingMetadata(i, propertyName, meta.getColumnClassName(i)));
					}
					found = true;
					break;
				}
			}
			
			//EmbeddedIdの可能性を探す
			if(!found){
				Set<Field> embFields = ClassUtils.getFields(EmbeddedId.class, type);
				for(Field f : embFields){
					if(isIgnoreField(f)){
						continue;
					}
					Class<?> pkClass = f.getType();
					Field[] pkFields = ClassUtils.getAllFields(pkClass);
					for(Field ff : pkFields){
						if(isIgnoreField(ff)){
							continue;
						}
						String propertyName = ff.getName();
						if (equalsColumnAnnotation(colName, propertyName,pkClass) ||
								equalsColumnProperty(colName, propertyName)) {
							if(!containsProperty(result, propertyName)){
								result.add(new MappingMetadata(i, f.getName() + "/" + propertyName,meta.getColumnClassName(i)));
							}
							found = true;
							break;
						}
					}
					break;
				}
			}
			
			//Embeddedの可能性を探す
			if(!found){
				Set<Field> embFields = ClassUtils.getFields(Embedded.class, type);
				for(Field f : embFields){
					if(isIgnoreField(f)){
						continue;
					}
					Class<?> pkClass = f.getType();
					Field[] pkFields = ClassUtils.getAllFields(pkClass);
					for(Field ff : pkFields){
						if(isIgnoreField(ff)){
							continue;
						}
						String propertyName = ff.getName();
						if (equalsColumnAnnotation(colName, propertyName,pkClass) ||
								equalsColumnProperty(colName, propertyName)) {
							if(!containsProperty(result, propertyName)){
								result.add(new MappingMetadata(i, f.getName() + "/" + propertyName, meta.getColumnClassName(i)));
							}
							found = true;
							break;
						}
					}
					break;
				}
			}
			
			if(!found){
				logger.warn("取得したカラムに対応するフィールドが見つかりませんでした。name={}",colName);
			}
		}
		
		return result;
	}
	
	private boolean isIgnoreField(Field f){
		int mod = f.getModifiers();
		if(Modifier.isStatic(mod) ||
				Modifier.isTransient(mod) ||
				Modifier.isNative(mod) ||
				Modifier.isVolatile(mod) ||
				f.isAnnotationPresent(Transient.class)){
			return true;
		}
		
		return false;
	}
	
	private boolean containsProperty(List<MappingMetadata> list,String propertyName){
		for(MappingMetadata t : list){
			if(t.propertyPath.equals(propertyName)){
				logger.warn("取得したカラム名が重複しているため、後から見つかった項目は無視されます property={}",propertyName);
				return true;
			}
		}
		return false;
	}
	
	private boolean equalsColumnAnnotation(String colName, String propName ,Class<?> type){
		Field f = ClassUtils.getField(propName, type);
		if(f == null){
			return false;
		}
		if(!f.isAnnotationPresent(Column.class)){
			return false;
		}
		
		Column c = f.getAnnotation(Column.class);
		String name = c.name();
		return colName.equalsIgnoreCase(name);
	}
	
	private boolean equalsColumnProperty(String colName, String propName) {
		//NOTE: 頻繁に呼び出されるので実際にはキャッシュを使うなどして
		//高速化したほうがよいです。
		return colName.replaceAll("_", "").equalsIgnoreCase(propName);
	}
	
	private static void callSetter(Field f,Object target,Object obj){
		//TODO プリミティブタイプの時にnullをセットしようとする場合エラーにしないとだめ
		try{
			ClassUtils.callSetter(f, f.getType(), target, obj);
		} catch (RuntimeException e) {
			logger.error(String.format("cannot call Setter %s.%s(%s)",target.getClass().getName(),f.getName(),obj.getClass().getName()),e);
			throw e;
		}
	}
	
	private static class MappingMetadata{
		private final int columnIndex;
		private final String propertyPath;
		private final Class<?> columnJavaType;
		
		MappingMetadata(int columnIndex, String propertyPath, String columnJavaTypeName) {
			this.columnIndex = columnIndex;
			this.propertyPath = propertyPath;
			try {
				this.columnJavaType = Class.forName(columnJavaTypeName);
			} catch (ClassNotFoundException e) {
				throw new QueryException("Unknown Class", e);
			}
		}
	}
}
