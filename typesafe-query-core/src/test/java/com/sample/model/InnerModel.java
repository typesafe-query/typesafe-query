package com.sample.model;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLXML;

import com.github.typesafe_query.annotation.Transient;
import com.sample.model.TypeModel.EN;

public class InnerModel {
	private Clob clob1;
	private Blob blob1;
	private SQLXML sqlxml;
	@Transient
	private EN enum1;
	public Clob getClob1() {
		return clob1;
	}
	public void setClob1(Clob clob1) {
		this.clob1 = clob1;
	}
	public Blob getBlob1() {
		return blob1;
	}
	public void setBlob1(Blob blob1) {
		this.blob1 = blob1;
	}
	public SQLXML getSqlxml() {
		return sqlxml;
	}
	public void setSqlxml(SQLXML sqlxml) {
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
