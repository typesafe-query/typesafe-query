package com.github.typesafe_query;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.esotericsoftware.yamlbeans.YamlConfig;
import com.esotericsoftware.yamlbeans.YamlReader;

public class Settings {
	
	private static final String SETTING_FILE_PATH = "/META-INF/typesafe-query.yml";
	private static Settings instance;
	
	public Settings() {
	}
	
	public static Settings get(){
		if(instance == null){
			InputStream in = Settings.class.getResourceAsStream(SETTING_FILE_PATH);
			if(in == null){
				instance = new Settings();
			}else{
				YamlConfig config = new YamlConfig();
				config.setPrivateFields(true);
				
				try {
					YamlReader reader = new YamlReader(new InputStreamReader(in, "UTF-8"),config);
					instance = reader.read(Settings.class);
					reader.close();
				} catch (IOException e) {
					//FIXME 例外をちゃんと決める
					throw new RuntimeException("Failed read settings.",e);
				}
			}
		}
		return instance;
	}
	
	private String dbType;
	private String resourceQueryCharset = "UTF-8";
	private String externalFileRoot = "/";

	public String getDbType() {
		return dbType;
	}

	public String getResourceQueryCharset() {
		return resourceQueryCharset;
	}

	public String getExternalFileRoot() {
		return externalFileRoot;
	}
}
