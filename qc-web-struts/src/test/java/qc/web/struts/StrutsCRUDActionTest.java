package qc.web.struts;

import org.apache.commons.beanutils.DynaBean;
import org.apache.struts.action.ActionForm;

import servletunit.struts.MockStrutsTestCase;

public class StrutsCRUDActionTest extends MockStrutsTestCase {
	public StrutsCRUDActionTest(String testName) {
		super(testName);
		// NOTE: By default, the Struts ActionServlet will look for the file
		// WEB-INF/struts-config.xml, so you must place the directory that
		// contains WEB-INF in your CLASSPATH. If you would like to use an
		// alternate configuration file, please see the setConfigFile() method
		// for details on how this file is located.
		
		//org.springframework.web.context.support.org.springframework.web.context.support.ROOT
		//XmlWebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE;
		//ContextLoaderPlugIn.SERVLET_CONTEXT_PREFIX;
		//new ModuleConfig();
	}

	protected void setUp() throws Exception {
		super.setUp();
		//setConfigFile("/WEB-INF/struts-config.xml");
//		context.setInitParameter(ContextLoader.CONFIG_LOCA TION_PARAM, 
//				getContextDirectory() + SPRING_CONTEXT_FILE_LOCATION);
//				new ContextLoader().initWebApplicationContext(context) ;	
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testOpenSuccess() {
		setRequestPathInfo("/example");
		addRequestParameter("method", "open");
		addRequestParameter("id", "1");

		actionPerform();
		verifyForward("ExampleForm");
		verifyNoActionErrors();

		ActionForm form = this.getActionForm();
		assertNotNull(form);
		assertTrue(form instanceof DynaBean);
		DynaBean bean = (DynaBean) form;
		assertEquals("1", bean.get("id"));
		assertEquals("name", bean.get("name"));
	}
}
