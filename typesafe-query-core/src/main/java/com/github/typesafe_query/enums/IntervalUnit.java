package com.github.typesafe_query.enums;

/**
 * 日付加減時に指定可能な単位
 */
public enum IntervalUnit {
	YEAR("YEAR"),
	QUARTER("QUARTER"),
	MONTH("MONTH"),
	WEEK("WEEK"),
	DAY("DAY"),
	HOUR("HOUR"),
	MINUTE("MINUTE"),
	SECOND("SECOND"),
	MICROSECOND("MICROSECOND"),;
	
	private final String unit;

	private IntervalUnit(final String unit) {
		this.unit = unit;
	}

	public String getString() {
		return this.unit;
	}
}
