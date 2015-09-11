package com.github.typesafe_query.meta;

import com.github.typesafe_query.Beta;
import com.github.typesafe_query.ModelDescription;

@Beta
public interface MetaClass<T> {
	Class<T> modelClass();
	IDBTable table();
	ModelDescription description();
}
