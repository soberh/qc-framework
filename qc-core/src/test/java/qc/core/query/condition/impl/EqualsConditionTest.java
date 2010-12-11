/**
 * 
 */
package qc.core.query.condition.impl;

import junit.framework.Assert;

import org.junit.Test;

import qc.core.query.condition.impl.EqualsCondition;

/**
 * 
 * @author dragon
 * 
 */
public class EqualsConditionTest {
	@Test
	public void test() {
		EqualsCondition c = new EqualsCondition("key", "value");
		Assert.assertEquals("key = ?", c.getExpression());
		Assert.assertNotNull(c.getValues());
		Assert.assertTrue(c.getValues().size() == 1);
		Assert.assertEquals("value", c.getValues().get(0));
	}
}
