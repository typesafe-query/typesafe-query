package com.github.typesafe_query;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.typesafe_query.annotation.Column;
import com.github.typesafe_query.annotation.EmbeddedId;
import com.github.typesafe_query.annotation.Id;
import com.github.typesafe_query.meta.DBTable;
import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.util.ClassUtils;
import com.github.typesafe_query.util.Pair;

public class ModelDescription<T> {
	private final Class<T> modelClass;
	private final DBTable table;
	
	private final boolean idGenerated;
	
	private final List<Pair<String, String>> idNames;
	private final List<Pair<String, String>> allNames;
	private final List<Pair<String, String>> valueNames;
	private final List<Exp> defaultExps;
	
	public ModelDescription(Class<T> modelClass,DBTable table,boolean idGenerated,List<String> fieldNames, Exp...defaultExps) {
		this.modelClass = modelClass;
		this.table = table;
		this.idGenerated = idGenerated;
		
		this.idNames = new ArrayList<Pair<String, String>>();
		this.allNames = new ArrayList<Pair<String, String>>();
		this.valueNames = new ArrayList<Pair<String, String>>();
		this.defaultExps = defaultExps != null ? Arrays.asList(defaultExps) : new ArrayList<>();
		
		for(String fieldName : fieldNames){
			if(fieldName.contains("/")){
				String[] subNames = fieldName.split("/");
				if(subNames.length != 2){
					throw new RuntimeException(String.format("フィールド名%sが不正です", fieldName));
				}
				Field f = ClassUtils.getField(subNames[0], modelClass);
				if(f == null){
					throw new RuntimeException(String.format("%sクラスのフィールド%sが見つかりません", modelClass.getSimpleName(),subNames[0]));
				}
				Class<?> emb = f.getType();
				Field ff = ClassUtils.getField(subNames[1], emb);
				if(f.isAnnotationPresent(EmbeddedId.class)){
					processColumn(ff,fieldName,true);
				}else{
					processColumn(ff,fieldName,false);
				}
			}else{
				Field f = ClassUtils.getField(fieldName, modelClass);
				if(f == null){
					throw new RuntimeException(String.format("%sクラスのフィールド%sが見つかりません", modelClass.getSimpleName(),fieldName));
				}
				
				//id検索
				if(f.isAnnotationPresent(Id.class)){
					processColumn(f,fieldName, true);
				}else{
					processColumn(f,fieldName,false);
				}
			}
		}
	}
	
	private void processColumn(Field f,String propName,boolean isId){
		String colName = camelToSnake(f.getName());
		if(f.isAnnotationPresent(Column.class)){
			Column c = f.getAnnotation(Column.class);
			if(c.name() != null && !c.name().isEmpty()){
				colName = c.name();
			}
		}
		
		Pair<String, String> t = new Pair<String,String>(colName, propName);
		allNames.add(t);
		if(isId){
			idNames.add(t);
		}else{
			valueNames.add(t);
		}
	}
	
	private String camelToSnake(String targetStr) {
		String convertedStr = targetStr
				.replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
				.replaceAll("([a-z])([A-Z])", "$1_$2");
		return convertedStr.toLowerCase();
	}
	
	public Class<T> getModelClass() {
		return modelClass;
	}

	public DBTable getTable() {
		return table;
	}

	public boolean isIdGenerated() {
		return idGenerated;
	}

	public List<Pair<String, String>> getIdNames(){
		return new ArrayList<Pair<String, String>>(idNames);
	}
	
	public List<Pair<String, String>> getAllNames(){
		return new ArrayList<Pair<String, String>>(allNames);
	}
	
	public List<Pair<String, String>> getValueNames(){
		return new ArrayList<Pair<String, String>>(valueNames);
	}

	public List<Exp> getDefaultExps() {
		return new ArrayList<>(defaultExps);
	}

}
