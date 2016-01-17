package com.sample.model;

import com.github.typesafe_query.Bulk;
import com.github.typesafe_query.DefaultBulk;
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
public final class UserInfo_{

	public static final DBTable TABLE = new DBTableImpl("user_info");
	public static final StringDBColumn USER_ID = new StringDBColumnImpl(TABLE,"user_id");
	private static final List<String> _FIELDS = Arrays.asList("userId");
	private static final ModelDescription<UserInfo> _DESC = new ModelDescription<>(UserInfo.class,TABLE, _FIELDS);
	private static final DefaultModelHandler<UserInfo> model = new DefaultModelHandler<>(_DESC);
	private static final Finder<String,UserInfo> find = new DefaultFinder<>(_DESC);
	private static final Bulk bulk = new DefaultBulk(TABLE);

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