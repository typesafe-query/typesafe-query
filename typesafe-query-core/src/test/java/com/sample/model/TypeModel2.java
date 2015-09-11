package com.sample.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

import com.github.typesafe_query.annotation.Embedded;


public class TypeModel2 {
	public static enum EN{
		ON,OFF;
	}
	
	private Optional<String> string;
	private Optional<Boolean> boolean2;
	private Optional<Byte> byte2;
	private Optional<Short> short2;
	private Optional<Integer> int2;
	private Optional<Long> long2;
	private Optional<Float> float2;
	private double double1;
	private Optional<Double> double2;
	private Optional<BigInteger> bigInteger1;
	private Optional<BigDecimal> bigDecimal;
	@Embedded
	private InnerModel innerModel;
	public Optional<String> getString() {
		return string;
	}
	public void setString(Optional<String> string) {
		this.string = string;
	}
	public Optional<Boolean> getBoolean2() {
		return boolean2;
	}
	public void setBoolean2(Optional<Boolean> boolean2) {
		this.boolean2 = boolean2;
	}
	public Optional<Byte> getByte2() {
		return byte2;
	}
	public void setByte2(Optional<Byte> byte2) {
		this.byte2 = byte2;
	}
	public Optional<Short> getShort2() {
		return short2;
	}
	public void setShort2(Optional<Short> short2) {
		this.short2 = short2;
	}
	public Optional<Integer> getInt2() {
		return int2;
	}
	public void setInt2(Optional<Integer> int2) {
		this.int2 = int2;
	}
	public Optional<Long> getLong2() {
		return long2;
	}
	public void setLong2(Optional<Long> long2) {
		this.long2 = long2;
	}
	public Optional<Float> getFloat2() {
		return float2;
	}
	public void setFloat2(Optional<Float> float2) {
		this.float2 = float2;
	}
	public double getDouble1() {
		return double1;
	}
	public void setDouble1(double double1) {
		this.double1 = double1;
	}
	public Optional<Double> getDouble2() {
		return double2;
	}
	public void setDouble2(Optional<Double> double2) {
		this.double2 = double2;
	}
	public Optional<BigInteger> getBigInteger1() {
		return bigInteger1;
	}
	public void setBigInteger1(Optional<BigInteger> bigInteger1) {
		this.bigInteger1 = bigInteger1;
	}
	public Optional<BigDecimal> getBigDecimal() {
		return bigDecimal;
	}
	public void setBigDecimal(Optional<BigDecimal> bigDecimal) {
		this.bigDecimal = bigDecimal;
	}
	public InnerModel getInnerModel() {
		return innerModel;
	}
	public void setInnerModel(InnerModel innerModel) {
		this.innerModel = innerModel;
	}
}
