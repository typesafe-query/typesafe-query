package com.github.typesafe_query.query;

public interface SearchedCase extends Case{
	SearchedCase when(Exp exp);
	SearchedCase then(Object o);
	SearchedCase else_(Object o);
}
