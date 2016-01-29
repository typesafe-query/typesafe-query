package com.sample.model;


import com.github.typesafe_query.annotation.AutoIncrement;
import com.github.typesafe_query.annotation.Id;
import com.github.typesafe_query.annotation.Table;


/**
 * The persistent class for the unit database table.
 * 
 */
@Table
public class GeneratedIdTable {

	@Id
	@AutoIncrement
	private Long id;

	private String name;


	public GeneratedIdTable() {
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}