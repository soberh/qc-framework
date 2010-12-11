/**
 * 
 */
package qc.core.query.condition.impl;

import qc.core.query.QueryOperator;

/**
 * 小于条件
 * 
 * @author dragon
 * 
 */
public class LessThanCondition extends SimpleCondition {
	public LessThanCondition(String name, Object value) {
		super(name, value, QueryOperator.LessThan);
	}
}
