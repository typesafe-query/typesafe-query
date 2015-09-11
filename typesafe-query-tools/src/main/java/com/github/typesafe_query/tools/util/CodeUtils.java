package com.github.typesafe_query.tools.util;


/**
 * @author MOSA Takahiko Satou
 *
 */
public final class CodeUtils {
	private CodeUtils(){}
	public static String toJavadocComment(String comment){
			if(comment == null){
					return "";
			}
			return "/** " + comment.replace("\n", " ") + " */\n";
	}

	public static String toMultiLineJavadocComment(String comment,int indent){
			if(comment == null){
					return "";
			}

			String[] lines = comment.split("\n",-1);
			StringBuilder sb = new StringBuilder();
			for(int i = 0;i < indent;i++){
					sb.append("\t");
			}
			sb.append("/**\n");
			for(String line : lines){
					for(int i = 0;i < indent;i++){
							sb.append("\t");
					}
					sb.append(" * ")
					  .append(line)
					  .append("\n");
			}
			for(int i = 0;i < indent;i++){
					sb.append("\t");
			}
			sb.append(" */").append("\n");
			return sb.toString();
	}

	public static String toVariableName(String propertyName){
			char c = propertyName.charAt(0);
			String head = String.valueOf(c).toLowerCase();
			return head + propertyName.substring(1);
	}

	public static String toAccessorMethodName(String propertyName){
			char c = propertyName.charAt(0);
			String head = String.valueOf(c).toUpperCase();
			return head + propertyName.substring(1);
	}

	public static String toClassName(String className){
			char c = className.charAt(0);
			String head = String.valueOf(c).toUpperCase();
			return head + className.substring(1);

	}

	public static String toFullyClassName(String className,String packageName){
			if(!packageName.endsWith(".")){
					packageName = packageName + ".";
			}
			return packageName + toClassName(className);
	}

	public static String toPluralForm(String propertyName){
			String endString;
			if(propertyName.endsWith("Parson") || propertyName.endsWith("parson")){
					propertyName = propertyName.replace("Parson", "");
					propertyName = propertyName.replace("parson", "");
					if("".equals(propertyName)){
							endString = "people";
					}else{
							endString = "People";
					}
			}else if(propertyName.endsWith("ch") ||
							propertyName.endsWith("th") ||
							propertyName.endsWith("s")){
					endString = "es";
			}else if(propertyName.endsWith("y") && propertyName.length()>1){
					String s = propertyName.substring(propertyName.length()-1, propertyName.length());
					if("a".equals(s) || "i".equals(s) || "u".equals(s) || "e".equals(s) || "o".equals(s)){
							endString = "s";
					}else{
							propertyName = propertyName.substring(0, propertyName.length()-1);
							endString = "ies";
					}

			}else{
					endString = "s";
			}

			return propertyName + endString;
	}
	
	public static String to_(String camelString){
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for(char c : camelString.toCharArray()){
			if(Character.isUpperCase(c) && index != 0){
				sb.append("_");
				c = Character.toLowerCase(c);
			}
			sb.append(c);
			index++;
		}
		return sb.toString();
	}
	
	public static String toCamel(String underScore){
		StringBuilder sb = new StringBuilder();
		int index = 0;
		boolean upper = false;
		for(char c : underScore.toCharArray()){
			if(c == '_' && index != 0){
				upper = true;
			}else if(upper){
				upper = false;
				sb.append(Character.toUpperCase(c));
			}else{
				sb.append(c);
			}
			index++;
		}
		return sb.toString();
	}
}