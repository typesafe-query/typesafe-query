/**
 * 
 */
package com.github.typesafe_query.query.internal.expression;

import com.github.typesafe_query.query.Exp;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class OrExp extends AndExp {

	public OrExp(Exp[] exps) {
		super(exps);
	}

	@Override
	protected String getJoinString() {
		return " OR ";
	}
}
