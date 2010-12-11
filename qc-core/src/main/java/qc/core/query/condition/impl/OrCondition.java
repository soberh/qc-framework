/**
 * 
 */
package qc.core.query.condition.impl;

import qc.core.query.condition.Condition;


/**
 * 以and(和)方式合并条件
 * @author dragon
 *
 */
public class OrCondition extends MixCondition implements Condition{
	public OrCondition(){
		super("or");
	}
	
	public OrCondition(Condition... conditions){
		super("or",conditions);
	}
}
