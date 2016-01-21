package com.sample.model;

import com.github.typesafe_query.Bulk;
import com.github.typesafe_query.DefaultBulk;
import java.util.Arrays;
import com.github.typesafe_query.meta.impl.DBTableImpl;
import com.github.typesafe_query.meta.impl.DateDBColumnImpl;
import com.github.typesafe_query.meta.impl.StringDBColumnImpl;
import com.github.typesafe_query.Finder;
import java.sql.Date;
import com.github.typesafe_query.meta.DBTable;
import java.time.LocalDate;
import com.github.typesafe_query.ModelDescription;
import java.util.List;
import com.github.typesafe_query.DefaultFinder;
import com.github.typesafe_query.ReusableModelHandler;
import com.github.typesafe_query.DefaultModelHandler;
import com.github.typesafe_query.meta.DateDBColumn;
import javax.annotation.Generated;
import com.github.typesafe_query.meta.StringDBColumn;

@Generated("Meta Generator")
public final class ApUser2_{

	public static final DBTable TABLE = new DBTableImpl("other","ap_user2");
	public static final StringDBColumn USER_ID = new StringDBColumnImpl(TABLE,"user_id");
	public static final StringDBColumn LOCK_FLG = new StringDBColumnImpl(TABLE,"lock_flg");
	public static final StringDBColumn NAME = new StringDBColumnImpl(TABLE,"name");
	public static final DateDBColumn<Date> VALID_FROM = new DateDBColumnImpl<java.sql.Date>(TABLE,"valid_from");
	public static final DateDBColumn<LocalDate> VALID_TO = new DateDBColumnImpl<>(TABLE,"valid_to");
	public static final StringDBColumn ROLE_ID = new StringDBColumnImpl(TABLE,"role_id");
	public static final StringDBColumn UNIT_ID = new StringDBColumnImpl(TABLE,"unit_id");
	private static final List<String> _FIELDS = Arrays.asList("userId","lockFlg","name","validFrom","validTo","roleId","unitId");
	private static final ModelDescription<ApUser2> _DESC = new ModelDescription<>(ApUser2.class,TABLE,_FIELDS);
	private static final DefaultModelHandler<ApUser2> model = new DefaultModelHandler<>(_DESC);
	private static final Finder<String,ApUser2> find = new DefaultFinder<>(_DESC);
	private static final Bulk bulk = new DefaultBulk(TABLE);

	private ApUser2_(){
	}

	public static DefaultModelHandler<ApUser2> model(){
		return model;
	}

	public static ReusableModelHandler<ApUser2> modelForReuse(){
		return new ReusableModelHandler<>(_DESC);
	}

	public static Finder<String,ApUser2> find(){
		return find;
	}

	public static Bulk bulk(){
		return bulk;
	}
	
	public static ModelDescription<ApUser2> description(){
		return _DESC;
	}
}