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
public final class UserInfo_{

	public static final IDBTable TABLE = new DBTableImpl("user_info");
	public static final IStringDBColumn USER_ID = new StringDBColumnImpl(TABLE,"user_id");
	private static final List<String> _FIELDS = Arrays.asList("userId");
	private static final ModelDescription<UserInfo> _DESC = new ModelDescription<>(UserInfo.class,TABLE, _FIELDS);
	private static final DefaultModelHandler<UserInfo> model = new DefaultModelHandler<>(_DESC);
	private static final Finder<String,UserInfo> find = new DefaultFinder<>(_DESC);
	private static final Bulk bulk = new Bulk(TABLE);

	private UserInfo_(){
	}

	public static DefaultModelHandler<UserInfo> model(){
		return model;
	}

	public static ReusableModelHandler<UserInfo> modelForReuse(){
		return new ReusableModelHandler<>(_DESC);
	}

	public static Finder<String,UserInfo> find(){
		return find;
	}

	public static Bulk bulk(){
		return bulk;
	}
	public static ModelDescription<UserInfo> description(){
		return _DESC;
	}
}