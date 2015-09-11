/**
 * 
 */
package com.github.typesafe_query.query.internal;

import com.github.typesafe_query.query.Param;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class ParamImpl implements Param {

	@Override
	public String getParameterMarker() {
		return "?";
	}
	
}
