package com.github.typesafe_query.query.internal;

import com.github.typesafe_query.meta.DBColumn;
import com.github.typesafe_query.query.SimpleCase;
import com.sample.model.ApUser_;

public class SimpleCaseTest {
	public void v(){
		case_(ApUser_.NAME)
			.when("2").then("ほげ")
			.when("3").then("ぴよ")
			.else_("はげ")
		.endAsStringColumn().as("VVB");
	}
	
	static <T> SimpleCase<T> case_(DBColumn<T> col){
		return null;
	}
}
