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
import com.github.typesafe_query.ModelHandler;
import javax.annotation.Generated;
import com.github.typesafe_query.meta.StringDBColumn;

@Generated("Meta Generator")
public final class Role_{

	public static final DBTable TABLE = new DBTableImpl("role");
	public static final StringDBColumn UNIT_ID = new StringDBColumnImpl(TABLE,"unit_id");
	public static final StringDBColumn ROLE_ID = new StringDBColumnImpl(TABLE,"role_id");
	public static final StringDBColumn NAME = new StringDBColumnImpl(TABLE,"name");
	private static final List<String> _FIELDS = Arrays.asList("id/unitId","id/roleId","name");
	private static final ModelDescription _DESC = new ModelDescription(Role.class, _FIELDS);
	private static final ModelHandler<Role> model = new ModelHandler<Role>(Role.class,TABLE,_DESC);
	private static final Finder<RolePK,Role> find = new DefaultFinder<com.sample.model.RolePK,Role>(Role.class,TABLE,_DESC);
	private static final Bulk bulk = new Bulk(TABLE);

	private Role_(){
	}

	public static ModelHandler<Role> model(){
		return model;
	}

	public static ReusableModelHandler<Role> modelForReuse(){
		return new ReusableModelHandler<>(Role.class,TABLE,_DESC);
	}

	public static Finder<RolePK,Role> find(){
		return find;
	}

	public static Bulk bulk(){
		return bulk;
	}


}