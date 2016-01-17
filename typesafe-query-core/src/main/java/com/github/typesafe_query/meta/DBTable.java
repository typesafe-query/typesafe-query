/**
 * 
 */
package com.github.typesafe_query.meta;

import com.github.typesafe_query.query.QueryContext;



/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public interface DBTable {
	String getName();
	String getAlias();
	String getQuery(QueryContext context);
}
