package com.sample.model;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLXML;
import java.util.Optional;

import com.sample.model.TypeModel.EN;

public class InnerModel2 {
	private Optional<Clob> clob1;
	private Optional<Blob> blob1;
	private Optional<SQLXML> sqlxml;
	private EN enum1;
	
	public Optional<Clob> getClob1() {
		return clob1;
	}
	public void setClob1(Optional<Clob> clob1) {
		this.clob1 = clob1;
	}
	public Optional<Blob> getBlob1() {
		return blob1;
	}
	public void setBlob1(Optional<Blob> blob1) {
		this.blob1 = blob1;
	}
	public Optional<SQLXML> getSqlxml() {
		return sqlxml;
	}
	public void setSqlxml(Optional<SQLXML> sqlxml) {
		this.sqlxml = sqlxml;
	}
	public EN getEnum1() {
		return enum1;
	}
	public void setEnum1(EN enum1) {
		this.enum1 = enum1;
	}
	public void setEnum1(String s){
		if("1".equals(s)){
			this.enum1 = EN.ON;
		}else{
			this.enum1 = EN.OFF;
		}
	}

}
