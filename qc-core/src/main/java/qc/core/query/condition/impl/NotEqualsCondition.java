/**
 * 
 */
package qc.core.query.condition.impl;

import qc.core.query.QueryOperator;

/**
 * 不相等条件
 * 
 * @author dragon
 * 
 */
public class NotEqualsCondition extends SimpleCondition {
	public NotEqualsCondition(String name, Object value) {
		super(name, value, QueryOperator.NotEquals);
	}
}
