package com.sample.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import com.github.typesafe_query.Bulk;
import com.github.typesafe_query.DefaultBulk;
import com.github.typesafe_query.DefaultFinder;
import com.github.typesafe_query.DefaultModelHandler;
import com.github.typesafe_query.Finder;
import com.github.typesafe_query.ModelDescription;
import com.github.typesafe_query.ModelHandler;
import com.github.typesafe_query.ReusableModelHandler;
import com.github.typesafe_query.meta.BooleanDBColumn;
import com.github.typesafe_query.meta.DBTable;
import com.github.typesafe_query.meta.DateDBColumn;
import com.github.typesafe_query.meta.MetaClass;
import com.github.typesafe_query.meta.NumberDBColumn;
import com.github.typesafe_query.meta.StringDBColumn;
import com.github.typesafe_query.meta.impl.BooleanDBColumnImpl;
import com.github.typesafe_query.meta.impl.DBTableImpl;
import com.github.typesafe_query.meta.impl.DateDBColumnImpl;
import com.github.typesafe_query.meta.impl.NumberDBColumnImpl;
import com.github.typesafe_query.meta.impl.StringDBColumnImpl;

@Generated("Meta Generator")

public final class TypeModel3_ implements MetaClass{
	public static final DBTable TABLE = new DBTableImpl("typemodel3");

	public static final StringDBColumn ASTERISK = new StringDBColumnImpl(TABLE, "*");
	public static final NumberDBColumn<Long> ID = new NumberDBColumnImpl<>(TABLE,"id");
	public static final StringDBColumn STRING = new StringDBColumnImpl(TABLE,"string");
	public static final BooleanDBColumn BOOLEAN1 = new BooleanDBColumnImpl(TABLE,"boolean1");
	public static final BooleanDBColumn BOOLEAN2 = new BooleanDBColumnImpl(TABLE,"boolean2");
	public static final NumberDBColumn<Integer> INT1 = new NumberDBColumnImpl<>(TABLE,"int1");
	public static final NumberDBColumn<Integer> INT2 = new NumberDBColumnImpl<>(TABLE,"int2");
	public static final NumberDBColumn<Short> SHORT1 = new NumberDBColumnImpl<>(TABLE,"short1");
	public static final NumberDBColumn<Short> SHORT2 = new NumberDBColumnImpl<>(TABLE,"short2");
	public static final NumberDBColumn<Long> LONG1 = new NumberDBColumnImpl<>(TABLE,"long1");
	public static final NumberDBColumn<Long> LONG2 = new NumberDBColumnImpl<>(TABLE,"long2");
	public static final NumberDBColumn<Float> FLOAT1 = new NumberDBColumnImpl<>(TABLE,"float1");
	public static final NumberDBColumn<Float> FLOAT2 = new NumberDBColumnImpl<>(TABLE,"float2");
	public static final NumberDBColumn<Double> DOUBLE1 = new NumberDBColumnImpl<>(TABLE,"double1");
	public static final NumberDBColumn<Double> DOUBLE2 = new NumberDBColumnImpl<>(TABLE,"double2");
	public static final NumberDBColumn<BigDecimal> BIG_DECIMAL = new NumberDBColumnImpl<>(TABLE,"bigDecimal");
	public static final DateDBColumn<LocalDate> DATE1 = new DateDBColumnImpl<>(TABLE,"date1");
	public static final DateDBColumn<Date> DATE2 = new DateDBColumnImpl<java.sql.Date>(TABLE,"date2");
	public static final DateDBColumn<LocalTime> TIME1 = new DateDBColumnImpl<>(TABLE,"time1");
	public static final DateDBColumn<Time> TIME2 = new DateDBColumnImpl<java.sql.Time>(TABLE,"time2");
	public static final DateDBColumn<LocalDateTime> TIMESTAMP1 = new DateDBColumnImpl<>(TABLE,"timestamp1");
	public static final DateDBColumn<Timestamp> TIMESTAMP2 = new DateDBColumnImpl<java.sql.Timestamp>(TABLE,"timestamp2");
	private static final List<String> _FIELDS = Arrays.asList("id","string","boolean1","boolean2","int1","int2","short1","short2","long1","long2","float1","float2","double1","double2","bigDecimal","date1","date2","time1","time2","timestamp1","timestamp2");
	private static final ModelDescription<TypeModel3> _DESC = new ModelDescription<>(TypeModel3.class,TABLE,false,_FIELDS,STRING.eq("1"), BOOLEAN1.eq(true), BOOLEAN2.eq(false), SHORT1.eq((short)1), SHORT2.eq((short)2), INT1.eq(3), INT2.eq(4), LONG1.eq(5L), LONG2.eq(6L), FLOAT1.eq(0.1F), FLOAT2.eq(0.2F), DOUBLE1.eq(0.3D), DOUBLE2.eq(0.4D), BIG_DECIMAL.eq(new BigDecimal(0.5)), DATE1.eq(LocalDate.of(2016,01,01)), TIME1.eq(LocalTime.of(11,11,11)), TIMESTAMP1.eq(LocalDateTime.of(2016,01,01,11,11,11,111000000)));
	private static final ModelHandler<TypeModel3> model = new DefaultModelHandler<>(_DESC);
	private static final Finder<Long,TypeModel3> find = new DefaultFinder<>(new DefaultFinder<>(_DESC));
	private static final Bulk bulk = new DefaultBulk(TABLE);

	private TypeModel3_(){
	}

	public static ModelDescription<TypeModel3> description(){
		return _DESC;
	}

	public static ModelHandler<TypeModel3> model(){
		return model;
	}

	public static ReusableModelHandler<TypeModel3> modelForReuse(){
		return new ReusableModelHandler<>(_DESC);
	}

	public static Finder<Long,TypeModel3> find(){
		return find;
	}

	public static Bulk bulk(){
		return bulk;
	}
}
