package com.sample.model;

import com.github.typesafe_query.Bulk;
import java.util.Arrays;
import com.github.typesafe_query.meta.impl.DBTableImpl;
import com.github.typesafe_query.meta.impl.StringDBColumnImpl;
import com.github.typesafe_query.Finder;
import com.github.typesafe_query.meta.IDBTable;
import com.github.typesafe_query.ModelDescription;
import java.util.List;
import com.github.typesafe_query.DefaultFinder;
import com.github.typesafe_query.ReusableModelHandler;
import com.github.typesafe_query.DefaultModelHandler;
import javax.annotation.Generated;
import com.github.typesafe_query.meta.IStringDBColumn;

@Generated("Meta Generator")
public final class Role_{

	public static final IDBTable TABLE = new DBTableImpl("role");
	public static final IStringDBColumn UNIT_ID = new StringDBColumnImpl(TABLE,"unit_id");
	public static final IStringDBColumn ROLE_ID = new StringDBColumnImpl(TABLE,"role_id");
	public static final IStringDBColumn NAME = new StringDBColumnImpl(TABLE,"name");
	private static final List<String> _FIELDS = Arrays.asList("id/unitId","id/roleId","name");
	private static final ModelDescription<Role> _DESC = new ModelDescription<>(Role.class,TABLE, _FIELDS);
	private static final DefaultModelHandler<Role> model = new DefaultModelHandler<>(_DESC);
	private static final Finder<RolePK,Role> find = new DefaultFinder<>(_DESC);
	private static final Bulk bulk = new Bulk(TABLE);

	private Role_(){
	}

	public static DefaultModelHandler<Role> model(){
		return model;
	}

	public static ReusableModelHandler<Role> modelForReuse(){
		return new ReusableModelHandler<>(_DESC);
	}

	public static Finder<RolePK,Role> find(){
		return find;
	}

	public static Bulk bulk(){
		return bulk;
	}

	public static ModelDescription<Role> description(){
		return _DESC;
	}
}