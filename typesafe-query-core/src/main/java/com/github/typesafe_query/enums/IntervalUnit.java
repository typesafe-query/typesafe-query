package com.github.typesafe_query.enums;

/**
 * 日付加加減時に指定可能な単位
 */
public enum IntervalUnit {
	MICROSECOND("MICROSECOND"),
	SECOND("SECOND"),
	MINUTE("MINUTE"),
	HOUR("HOUR"),
	DAY("DAY"),
	WEEK("WEEK"),
	MONTH("MONTH"),
	QUARTER("QUARTER"),
	YEAR("YEAR"),;
	
	private final String unit;

	private IntervalUnit(final String unit) {
		this.unit = unit;
	}

	public String getString() {
		return this.unit;
	}
}
