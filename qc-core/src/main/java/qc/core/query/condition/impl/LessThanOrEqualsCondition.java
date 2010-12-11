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
public class LessThanOrEqualsCondition extends SimpleCondition {
	public LessThanOrEqualsCondition(String name, Object value) {
		super(name, value, QueryOperator.LessThanOrEquals);
	}
}
