package com.github.typesafe_query.util;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

//TODO v0.3.x TimeZoneの話
public final class DateTimeUtils {
	private DateTimeUtils() {}
	
	
	public static Date toDate(LocalDate localDate){
		if(localDate == null){
			return null;
		}
		return new Date(Date.from(localDate.atStartOfDay(ZoneOffset.systemDefault()).toInstant()).getTime());
	}
	
	public static Time toTime(LocalTime localTime){
		if(localTime == null){
			return null;
		}
		return new Time(Time.from(ZonedDateTime.of(LocalDate.now(), localTime, ZoneOffset.systemDefault()).toInstant()).getTime());
	}
	
	public static Timestamp toTimestamp(LocalDateTime localDateTime){
		if(localDateTime == null){
			return null;
		}
		return java.sql.Timestamp.from(ZonedDateTime.of(localDateTime, ZoneOffset.systemDefault()).toInstant());
	}
	
	public static LocalDate toLocalDate(Date date){
		if(date == null){
			return null;
		}
		LocalDateTime ldt = LocalDateTime.ofInstant(new java.util.Date(date.getTime()).toInstant(), ZoneOffset.systemDefault());
		return ldt.toLocalDate();
	}
	
	public static LocalTime toLocalTime(Time time){
		if(time == null){
			return null;
		}
		LocalDateTime ldt = LocalDateTime.ofInstant(new java.util.Date(time.getTime()).toInstant(), ZoneOffset.systemDefault());
		return ldt.toLocalTime();
	}
	
	public static LocalDateTime toLocalDateTime(Timestamp timestamp){
		if(timestamp == null){
			return null;
		}
		return LocalDateTime.ofInstant(timestamp.toInstant(), ZoneOffset.systemDefault());
	}
}
