package com.sample.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import com.github.typesafe_query.ModelBase;
import com.github.typesafe_query.annotation.Column;
import com.github.typesafe_query.annotation.Id;
import com.github.typesafe_query.annotation.Table;


/**
 * The persistent class for the ap_user database table.
 * 
 */ 
@Table(schema="other")
public class ApUser2 extends ModelBase<ApUser2>{
	@Id
	private String userId;

	@Column(name="lock_flg")
	private String lockFlg;

	private String name;

	@Column(name="valid_from")
	private Optional<Date> validFrom = Optional.empty();

	@Column(name="valid_to")
	private Optional<LocalDate> validTo = Optional.empty();

	@Column(name="role_id")
	private String roleId;
	
	@Column(name="unit_id")
	private String unitId;
	
	public ApUser2() {
		super(ApUser2_.description());
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLockFlg() {
		return this.lockFlg;
	}

	public void setLockFlg(String lockFlg) {
		this.lockFlg = lockFlg;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Optional<Date> getValidFrom() {
		return this.validFrom;
	}

	public void setValidFrom(Optional<Date> validFrom) {
		this.validFrom = validFrom;
	}

	public Optional<LocalDate> getValidTo() {
		return this.validTo;
	}

	public void setValidTo(Optional<LocalDate> validTo) {
		this.validTo = validTo;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	
}