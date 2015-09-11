package com.github.typesafe_query;

public class Settings {
	
	private static Settings instance;
	
	public Settings() {
	}
	
	public static Settings get(){
		if(instance == null){
			instance = new Settings();
		}
		return instance;
	}
	
	private String dbType;
	private String namedQueryCharset = "UTF-8";

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getNamedQueryCharset() {
		return namedQueryCharset;
	}

	public void setNamedQueryCharset(String namedQueryCharset) {
		this.namedQueryCharset = namedQueryCharset;
	}
}
