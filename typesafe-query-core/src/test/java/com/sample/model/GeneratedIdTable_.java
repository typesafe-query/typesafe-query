package com.sample.model;

import com.github.typesafe_query.Bulk;
import com.github.typesafe_query.DefaultBulk;
import java.util.Arrays;
import com.github.typesafe_query.meta.impl.DBTableImpl;
import com.github.typesafe_query.meta.impl.StringDBColumnImpl;
import com.github.typesafe_query.Finder;
import com.github.typesafe_query.meta.DBTable;
import com.github.typesafe_query.meta.MetaClass;
import com.github.typesafe_query.ModelDescription;
import java.util.List;
import com.github.typesafe_query.DefaultFinder;
import com.github.typesafe_query.ReusableModelHandler;
import com.github.typesafe_query.DefaultModelHandler;
import javax.annotation.Generated;
import com.github.typesafe_query.meta.StringDBColumn;

@Generated("Meta Generator")
public final class GeneratedIdTable_ implements MetaClass{

	public static final DBTable TABLE = new DBTableImpl("generated_id_table");
	public static final StringDBColumn ID = new StringDBColumnImpl(TABLE,"id");
	public static final StringDBColumn NAME = new StringDBColumnImpl(TABLE,"name");
	private static final List<String> _FIELDS = Arrays.asList("id","name");
	private static final ModelDescription<GeneratedIdTable> _DESC = new ModelDescription<>(GeneratedIdTable.class,TABLE, true,_FIELDS);
	private static final DefaultModelHandler<GeneratedIdTable> model = new DefaultModelHandler<>(_DESC);
	private static final Finder<String,GeneratedIdTable> find = new DefaultFinder<>(_DESC);
	private static final Bulk bulk = new DefaultBulk(TABLE);

	private GeneratedIdTable_(){
	}

	public static DefaultModelHandler<GeneratedIdTable> model(){
		return model;
	}

	public static ReusableModelHandler<GeneratedIdTable> modelForReuse(){
		return new ReusableModelHandler<>(_DESC);
	}

	public static Finder<String,GeneratedIdTable> find(){
		return find;
	}

	public static Bulk bulk(){
		return bulk;
	}
	public static ModelDescription<GeneratedIdTable> description(){
		return _DESC;
	}
}