/**
 * 
 */
package qc.web.struts2;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import qc.core.SetEntityClass;
import qc.core.service.CrudService;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 必须通过spring进行配置才能实现注入entityClass
 * 
 * @author dragon
 * 
 */
/**
 * @author dragon
 * 
 * @param <T>
 */
public class StrutsCRUDAction<T extends Object> extends ActionSupport implements
		InitializingBean, SetEntityClass<T> {
	private static final long serialVersionUID = -1428717283670421814L;
	protected final Log logger = LogFactory.getLog(getClass());
	private CrudService<T> crudService;
	private Class<T> entityClass;
	private T entity;
	private String id;
	private boolean readOnly;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	protected Serializable parseId(String id) {
		return new Long(id);
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	@Autowired
	@Required
	public void setCrudService(CrudService<T> crudService) {
		this.crudService = crudService;
	}

	@SuppressWarnings("unchecked")
	public void afterPropertiesSet() throws Exception {
		if (this.crudService.getEntityClass() == null) {
			if (this.crudService instanceof SetEntityClass) {
				if (logger.isDebugEnabled())
					logger.debug("reset crudService's entityClass to '"
							+ entityClass + "'");
				// 补充设置crudService的entityClass
				((SetEntityClass<T>) this.crudService)
						.setEntityClass(entityClass);
			}
		}
	}

	protected Class<T> getEntityClass() {
		return this.entityClass;
	}

	public void setEntityClass(Class<T> clazz) {
		this.entityClass = clazz;
	}

	/**
	 * @return service接口的实现
	 */
	public CrudService<T> getCrudService() {
		return this.crudService;
	}

	public String execute() {
		return SUCCESS;
	}

	public String open() {
		Serializable id = parseId(this.getId());
		this.entity = this.crudService.load(id);
		return "input";
	}

	public String edit() {
		return null;
	}

	public String save() {
		return null;
	}

	public void validate() {
		//定义外在的bean来进行检验
	}

	public String delete() {
		return null;
	}

	public String view() {
		return null;
	}
}
