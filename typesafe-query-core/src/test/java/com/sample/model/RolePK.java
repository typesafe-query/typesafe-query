package com.sample.model;

import java.io.Serializable;

import com.github.typesafe_query.annotation.Column;

/**
 * The primary key class for the role database table.
 * 
 */
public class RolePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="unit_id")
	private String unitId;

	@Column(name="role_id")
	private String roleId;

	public RolePK() {
	}
	public String getUnitId() {
		return this.unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getRoleId() {
		return this.roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RolePK)) {
			return false;
		}
		RolePK castOther = (RolePK)other;
		return 
			this.unitId.equals(castOther.unitId)
			&& this.roleId.equals(castOther.roleId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.unitId.hashCode();
		hash = hash * prime + this.roleId.hashCode();
		
		return hash;
	}
}