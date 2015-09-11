package com.github.typesafe_query.tools;

import java.util.List;
import java.util.Map.Entry;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

public class Utils {
	private Utils() {}
	
	public static TypeElement findSuper(TypeElement t,String name){
		TypeMirror tm = t.getSuperclass();
		if(!(tm instanceof DeclaredType)){
			return null;
		}
		DeclaredType superClass = (DeclaredType)t.getSuperclass();
		TypeElement te = (TypeElement)superClass.asElement();
		if(name.equals(te.getQualifiedName().toString())){
			return te;
		}
		if("java.lang.Object".equals(te.getQualifiedName().toString())){
			return null;
		}
		return findSuper(te, name);
	}
	
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
	
	public static boolean hasAnnotation(Element e ,String name){
		return getAnnotation(e, name)!=null;
	}
	
	public static AnnotationMirror getAnnotation(Element e ,String name){
		List<? extends AnnotationMirror> mirrors = e.getAnnotationMirrors();
		for(AnnotationMirror am : mirrors){
			TypeElement te = (TypeElement) am.getAnnotationType().asElement();
			if(te.getQualifiedName().toString().equals(name)){
				return am;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getAnnotationPropertyValue(AnnotationMirror mirror,String name){
		for(Entry<? extends ExecutableElement, ? extends AnnotationValue> e : mirror.getElementValues().entrySet()){
			String nm = e.getKey().getSimpleName().toString();
			if(nm.equals(name)){
				return (T)e.getValue().getValue();
			}
		}
		return null;
	}
}
