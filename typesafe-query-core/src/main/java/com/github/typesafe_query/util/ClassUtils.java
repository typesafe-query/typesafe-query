package com.github.typesafe_query.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * {@link Class}に関するユーティリティクラスです
 * 
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public final class ClassUtils {
	
	private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_MAP = new HashMap<>();
	
	static{
		PRIMITIVE_WRAPPER_MAP.put(Byte.TYPE, Byte.class);
		PRIMITIVE_WRAPPER_MAP.put(Short.TYPE, Short.class);
		PRIMITIVE_WRAPPER_MAP.put(Integer.TYPE, Integer.class);
		PRIMITIVE_WRAPPER_MAP.put(Long.TYPE, Long.class);
		PRIMITIVE_WRAPPER_MAP.put(Float.TYPE, Float.class);
		PRIMITIVE_WRAPPER_MAP.put(Byte.TYPE, Byte.class);
		PRIMITIVE_WRAPPER_MAP.put(Double.TYPE, Double.class);
		PRIMITIVE_WRAPPER_MAP.put(Boolean.TYPE, Boolean.class);
	}
	
	private ClassUtils() {}
	
	/**
	 * {@link Class#forName(String)}を安全に実行します
	 * @param name クラス名
	 * @return {@link Class}オブジェクト
	 */
	public static Class<?> forName(String name){
		return forName(name, null);
	}
	
	/**
	 * {@link Class#forName(String)}を安全に実行します
	 * @param name クラス名
	 * @param loader クラスローダー
	 * @return {@link Class}オブジェクト
	 */
	public static Class<?> forName(String name,ClassLoader loader){
		try {
			if(loader == null){
				return Class.forName(name);
			}
			return Class.forName(name,true,loader);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@link Class#newInstance()}を安全に実行します
	 * @param name クラス名
	 * @return {@link Class}が表すクラスのインスタンス
	 */
	public static Object newInstance(String name){
		return newInstance(forName(name));
	}
	
	/**
	 * {@link Class#newInstance()}を安全に実行します
	 * @param name クラス名
	 * @param loader クラスローダー
	 * @return {@link Class}が表すクラスのインスタンス
	 */
	public static Object newInstance(String name,ClassLoader loader){
		return newInstance(forName(name,loader));
	}
	
	/**
	 * {@link Class#newInstance()}を安全に実行します
	 * @param clazz クラス
	 * @param <T> クラスの型
	 * @return {@link Class}が表すクラスのインスタンス
	 */
	public static <T>T newInstance(Class<T> clazz){
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@link Method}を取得します
	 * @param methodName メソッド名
	 * @param paramTypes 引数の型
	 * @param targetClass メソッドを持つクラスの{@link Class}オブジェクト
	 * @return {@link Method}
	 */
	public static Method getMethod(String methodName,Class<?>[] paramTypes,Class<?> targetClass){
		try {
			return targetClass.getDeclaredMethod(methodName, paramTypes);
		} catch (Exception e) {
			Class<?> superClass = targetClass.getSuperclass();
			if(superClass == null || superClass.equals(Object.class)){
				return null;
			}
			return getMethod(methodName, paramTypes, superClass);
		}
	}
	
	/**
	 * {@link Field}を返します
	 * @param fieldName フィールド名称
	 * @param targetClass フィールドを持つクラスの{@link Class}オブジェクト
	 * @return {@link Field}
	 */
	public static Field getField(String fieldName,Class<?> targetClass){
		try {
			return targetClass.getDeclaredField(fieldName);
		} catch (Exception e) {
			//Fix:searching parent Class
			Class<?> superClass = targetClass.getSuperclass();
			if(superClass == null || superClass.equals(Object.class)){
				return null;
			}
			return getField(fieldName, superClass);
		} 
	}
	
	public static Set<Field> getFields(Class<? extends Annotation> anot,Class<?> targetClass){
		Set<Field> fields = new HashSet<Field>();
		if(targetClass.equals(Object.class)){
			return fields;
		}
		for(Field f : targetClass.getDeclaredFields()){
			if(f.isAnnotationPresent(anot)){
				fields.add(f);
			}
		}
		fields.addAll(getFields(anot, targetClass.getSuperclass()));
		return fields;
	}
	
	/**
	 * スーパークラスを再帰して{@link Field}の配列を返します
	 * @param clazz 対象のクラス
	 * @return フィールド
	 */
	public static Field[] getAllFields(Class<?> clazz){
		List<Field> fields = new ArrayList<Field>();
		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		if(clazz.getSuperclass() != Object.class){
			fields.addAll(Arrays.asList(getAllFields(clazz.getSuperclass())));
		}
		return fields.toArray(new Field[0]);
	}	
	
	/**
	 * 対象フィールドのセッターメソッドをコールします
	 * @param f 対象フィールド
	 * @param argType 引数の型
	 * @param target 対象フィールドを持つオブジェクトのインスタンス
	 * @param obj 対象フィールにセットするオブジェクト
	 */
	public static void callSetter(Field f,Class<?> argType,Object target,Object obj){
		String setterName = toSetterName(f.getName());
		Method m = getMethod(setterName, new Class[]{argType}, target.getClass());
		if(m != null){
			invoke(m, target, obj);
		}
	}
	
	public static Object callGetter(Field f,Object target){
		String getterName = toGetterName(f);
		Method m = getMethod(getterName, new Class[]{}, target.getClass());
		if(m != null){
			return invoke(m, target);
		}
		return null;
	}

	
	/**
	 * クラス名を変数名にします
	 * @param className クラス名
	 * @return 変数名
	 */
	public static String toVariableName(String className){
		if(className == null || className.equals("")){
			throw new IllegalArgumentException("argument is empty");
		}
		String ret = className.substring(0,1).toLowerCase();
		if(className.length() > 1){
			ret = ret + className.substring(1);
		}
		return ret;
	}
	
	/**
	 * フィールド名からセッターメソッド名の文字列表現を返します
	 * @param fieldName フィールド名
	 * @return セッターメソッド名
	 */
	public static String toSetterName(String fieldName){
		return toAccessorName(fieldName, "set");
	}
	
	public static String toGetterName(Field f){
		if(f.getType().equals(Boolean.class) || f.getType().equals(boolean.class)){
			return toAccessorName(f.getName(), "is");
		}
		return toAccessorName(f.getName(), "get");
	}
	
	private static String toAccessorName(String fieldName,String accessorTypeName){
		if(fieldName == null || fieldName.equals("")){
			throw new IllegalArgumentException("argument is empty");
		}
		String ret = fieldName.substring(0,1).toUpperCase();
		if(fieldName.length() > 1){
			ret = ret + fieldName.substring(1);
		}
		return accessorTypeName + ret;
	}
	
	/**
	 * メソッドを安全に実行します
	 * @param m メソッド
	 * @param target メソッドを持つクラスのインスタンス
	 * @param params メソッド引数
	 * @return 実行したメソッドのreturn値
	 */
	public static Object invoke(Method m,Object target,Object...params){
		try {
			m.setAccessible(true);
			return m.invoke(target, params);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 指定したクラスが存在するかどうかを返します。
	 * @param className クラス名
	 * @return 存在する場合<code>true</code>、存在しない場合<code>false</code>
	 */
	public static boolean classExists(String className){
		try {
			forName(className);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static Class<?> primitiveToWrapperClass(Class<?> clazz){
		if(!clazz.isPrimitive()){
			return clazz;
		}
		if(PRIMITIVE_WRAPPER_MAP.containsKey(clazz)){
			return PRIMITIVE_WRAPPER_MAP.get(clazz);
		}
		throw new RuntimeException("対応していないプリミティブタイプです " + clazz);
	}
}
