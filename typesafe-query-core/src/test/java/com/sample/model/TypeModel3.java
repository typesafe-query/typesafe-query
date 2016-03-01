package com.sample.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.github.typesafe_query.annotation.DefaultWhere;
import com.github.typesafe_query.annotation.Id;

public class TypeModel3 {
	@Id
	private Long id;

	@DefaultWhere(value="1")
	private String string;
	
	@DefaultWhere(value="true")
	private boolean boolean1;
	@DefaultWhere(value="false")
	private Boolean boolean2;

	@DefaultWhere(value="1")
	private short short1;
	@DefaultWhere(value="2")
	private Short short2;
	
	@DefaultWhere(value="3")
	private int int1;
	@DefaultWhere(value="4")
	private Integer int2;
	
	@DefaultWhere(value="5")
	private long long1;
	@DefaultWhere(value="6")
	private Long long2;
	
	@DefaultWhere(value="0.1")
	private float float1;
	@DefaultWhere(value="0.2")
	private Float float2;

	@DefaultWhere(value="0.3")
	private double double1;
	@DefaultWhere(value="0.4")
	private Double double2;
	
	@DefaultWhere(value="0.5")
	private BigDecimal bigDecimal;
	
	@DefaultWhere(value="2016,01,01")
	private LocalDate date1;
	@DefaultWhere(value="2016-01-02")
	private Date date2;
	
	@DefaultWhere(value="11,11,11")
	private LocalTime time1;
	@DefaultWhere(value="12:12:12")
	private Time time2;
	
	@DefaultWhere(value="2016,01,01,11,11,11,111")
	private LocalDateTime timestamp1;
	@DefaultWhere(value="2016-01-02 12:12:12.222")
	private Timestamp timestamp2;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public BigDecimal getBigDecimal() {
		return bigDecimal;
	}
	public void setBigDecimal(BigDecimal bigDecimal) {
		this.bigDecimal = bigDecimal;
	}
	public LocalDate getDate1() {
		return date1;
	}
	public void setDate1(LocalDate date1) {
		this.date1 = date1;
	}
	public Date getDate2() {
		return date2;
	}
	public void setDate2(Date date2) {
		this.date2 = date2;
	}
	public LocalTime getTime1() {
		return time1;
	}
	public void setTime1(LocalTime time1) {
		this.time1 = time1;
	}
	public Time getTime2() {
		return time2;
	}
	public void setTime2(Time time2) {
		this.time2 = time2;
	}
	public LocalDateTime getTimestamp1() {
		return timestamp1;
	}
	public void setTimestamp1(LocalDateTime timestamp1) {
		this.timestamp1 = timestamp1;
	}
	public Timestamp getTimestamp2() {
		return timestamp2;
	}
	public void setTimestamp2(Timestamp timestamp2) {
		this.timestamp2 = timestamp2;
	}
}
