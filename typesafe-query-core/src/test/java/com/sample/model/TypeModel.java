package com.sample.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.github.typesafe_query.annotation.Converter;
import com.github.typesafe_query.annotation.Embedded;
import com.github.typesafe_query.annotation.Transient;
import com.github.typesafe_query.jdbc.convert.extra.Char1ToBooleanTypeConverter;


public class TypeModel {
	public static enum EN{
		ON,OFF;
	}
	
	private String string;
	@Converter(Char1ToBooleanTypeConverter.class)
	private boolean boolean1;
	@Converter(Char1ToBooleanTypeConverter.class)
	private Boolean boolean2;
	@Transient
	private byte byte1;
	@Transient
	private Byte byte2;
	private short short1;
	private Short short2;
	private int int1;
	private Integer int2;
	private long long1;
	private Long long2;
	private float float1;
	private Float float2;
	private double double1;
	private Double double2;
	@Transient
	private BigInteger bigInteger1;
	private BigDecimal bigDecimal;
	@Embedded
	private InnerModel innerModel;
	
	private LocalDate date1;
	private LocalTime time1;
	private LocalDateTime timestamp1;
	
	
	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
	}
	public boolean isBoolean1() {
		return boolean1;
	}
	public void setBoolean1(boolean boolean1) {
		this.boolean1 = boolean1;
	}
	public Boolean getBoolean2() {
		return boolean2;
	}
	public void setBoolean2(Boolean boolean2) {
		this.boolean2 = boolean2;
	}
	public byte getByte1() {
		return byte1;
	}
	public void setByte1(byte byte1) {
		this.byte1 = byte1;
	}
	public Byte getByte2() {
		return byte2;
	}
	public void setByte2(Byte byte2) {
		this.byte2 = byte2;
	}
	public short getShort1() {
		return short1;
	}
	public void setShort1(short short1) {
		this.short1 = short1;
	}
	public Short getShort2() {
		return short2;
	}
	public void setShort2(Short short2) {
		this.short2 = short2;
	}
	public int getInt1() {
		return int1;
	}
	public void setInt1(int int1) {
		this.int1 = int1;
	}
	public Integer getInt2() {
		return int2;
	}
	public void setInt2(Integer int2) {
		this.int2 = int2;
	}
	public long getLong1() {
		return long1;
	}
	public void setLong1(long long1) {
		this.long1 = long1;
	}
	public Long getLong2() {
		return long2;
	}
	public void setLong2(Long long2) {
		this.long2 = long2;
	}
	public float getFloat1() {
		return float1;
	}
	public void setFloat1(float float1) {
		this.float1 = float1;
	}
	public Float getFloat2() {
		return float2;
	}
	public void setFloat2(Float float2) {
		this.float2 = float2;
	}
	public double getDouble1() {
		return double1;
	}
	public void setDouble1(double double1) {
		this.double1 = double1;
	}
	public Double getDouble2() {
		return double2;
	}
	public void setDouble2(Double double2) {
		this.double2 = double2;
	}
	public BigInteger getBigInteger1() {
		return bigInteger1;
	}
	public void setBigInteger1(BigInteger bigInteger1) {
		this.bigInteger1 = bigInteger1;
	}
	public BigDecimal getBigDecimal() {
		return bigDecimal;
	}
	public void setBigDecimal(BigDecimal bigDecimal) {
		this.bigDecimal = bigDecimal;
	}
	public InnerModel getInnerModel() {
		return innerModel;
	}
	public void setInnerModel(InnerModel innerModel) {
		this.innerModel = innerModel;
	}
	public LocalDate getDate1() {
		return date1;
	}
	public void setDate1(LocalDate date1) {
		this.date1 = date1;
	}
	public LocalTime getTime1() {
		return time1;
	}
	public void setTime1(LocalTime time1) {
		this.time1 = time1;
	}
	public LocalDateTime getTimestamp1() {
		return timestamp1;
	}
	public void setTimestamp1(LocalDateTime timestamp1) {
		this.timestamp1 = timestamp1;
	}
	
}
