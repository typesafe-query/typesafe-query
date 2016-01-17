package com.sample.model;

import com.github.typesafe_query.Bulk;
import java.util.Arrays;
import com.github.typesafe_query.meta.impl.DBTableImpl;
import com.github.typesafe_query.meta.impl.StringDBColumnImpl;
import com.github.typesafe_query.Finder;
import com.github.typesafe_query.meta.DBTable;
import com.github.typesafe_query.ModelDescription;
import java.util.List;
import com.github.typesafe_query.DefaultFinder;
import com.github.typesafe_query.ReusableModelHandler;
import com.github.typesafe_query.DefaultModelHandler;
import javax.annotation.Generated;
import com.github.typesafe_query.meta.StringDBColumn;

@Generated("Meta Generator")
public final class Unit_{

	public static final DBTable TABLE = new DBTableImpl("unit");
	public static final StringDBColumn UNIT_ID = new StringDBColumnImpl(TABLE,"unit_id");
	public static final StringDBColumn NAME = new StringDBColumnImpl(TABLE,"name");
	private static final List<String> _FIELDS = Arrays.asList("unitId","name");
	private static final ModelDescription<Unit> _DESC = new ModelDescription<>(Unit.class,TABLE, _FIELDS);
	private static final DefaultModelHandler<Unit> model = new DefaultModelHandler<>(_DESC);
	private static final Finder<String,Unit> find = new DefaultFinder<>(_DESC);
	private static final Bulk bulk = new Bulk(TABLE);

	private Unit_(){
	}

	public static DefaultModelHandler<Unit> model(){
		return model;
	}

	public static ReusableModelHandler<Unit> modelForReuse(){
		return new ReusableModelHandler<>(_DESC);
	}

	public static Finder<String,Unit> find(){
		return find;
	}

	public static Bulk bulk(){
		return bulk;
	}
	public static ModelDescription<Unit> description(){
		return _DESC;
	}
}