/**
 * 
 */
package qc.core.query.condition.impl;

import junit.framework.Assert;

import org.junit.Test;

import qc.core.query.condition.impl.EqualsCondition;
import qc.core.query.condition.impl.OrCondition;

/**
 * 
 * @author dragon
 * 
 */
public class OrConditionTest {
	@Test
	public void construct() {
		OrCondition and = new OrCondition(new EqualsCondition("key1",
				"value1"), new EqualsCondition("key2", "value2"));
		Assert.assertEquals("key1 = ? or key2 = ?", and.getExpression());
		Assert.assertNotNull(and.getValues());
		Assert.assertTrue(and.getValues().size() == 2);
		Assert.assertEquals("value1", and.getValues().get(0));
		Assert.assertEquals("value2", and.getValues().get(1));
	}

	@Test
	public void add() {
		OrCondition or = new OrCondition();
		or.add(new EqualsCondition("key1", "value1"))
			.add(new EqualsCondition("key2", "value2"));
		Assert.assertEquals("key1 = ? or key2 = ?", or.getExpression());
		Assert.assertNotNull(or.getValues());
		Assert.assertTrue(or.getValues().size() == 2);
		Assert.assertEquals("value1", or.getValues().get(0));
		Assert.assertEquals("value2", or.getValues().get(1));
	}
}
