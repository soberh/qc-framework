package qc.web.struts2;

import junit.framework.Assert;

import org.apache.struts2.StrutsSpringTestCase;

import qc.core.service.CrudService;
import qc.test.Example;

import com.opensymphony.xwork2.ActionProxy;

public class StrutsCRUDActionTest extends StrutsSpringTestCase {
	private CrudService<Example> crudService;

	@Override
	protected String getContextLocations() {
		return "classpath:qc/web/struts2/StrutsCRUDActionTest-context.xml";
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.crudService = (CrudService<Example>) applicationContext
				.getBean("crudService");
	}

	@SuppressWarnings("unchecked")
	public void testOpenSuccess() throws Exception {
		// 保存一条记录
		Example entity = new Example("dragon");
		crudService.save(entity);
		Long id = entity.getId();
		Assert.assertNotNull(id);
		Assert.assertTrue(id > 0);

		request.setParameter("id", id.toString());

		ActionProxy proxy = getActionProxy("/example!open");
		StrutsCRUDAction<Example> crudAction = (StrutsCRUDAction<Example>) proxy
				.getAction();
		String result = proxy.execute();

		assertTrue(crudAction.getFieldErrors().size() == 0);
		assertEquals("success", result);

		// 验证
		entity = crudAction.getEntity();
		Assert.assertNotNull(entity);
		Assert.assertEquals(id, entity.getId());
		Assert.assertEquals("dragon", entity.getName());
		Assert.assertEquals(true, crudAction.isReadOnly());
	}
}
