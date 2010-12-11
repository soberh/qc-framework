package qc.orm.hibernate.jpa;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.util.StringUtils;

import qc.core.SetEntityClass;
import qc.core.dao.CrudDao;

/**
 * CrudDao的SpringHibernateJPA实现
 * 
 * @author dragon
 * 
 * @param <T>
 * @param <PK>
 */
public class HibernateCrudJpaDao<T extends Object> implements CrudDao<T>,
		SetEntityClass<T> {
	protected final Log logger = LogFactory.getLog(getClass());
	protected Class<T> entityClass;

	protected String pkName = "id";// 主键名称
	private JpaTemplate jpaTemplate;

	/**
	 * 通过构造函数注入实体对象的类型, 因为Java的泛型无法使用T.class的缘故
	 * 
	 * @param clazz
	 */
	public HibernateCrudJpaDao(Class<T> clazz) {
		this.entityClass = clazz;
	}

	/**
	 * 如果子类将泛型参数具体化了，这个构造函数用于自动侦测实体对象的类型
	 */
	@SuppressWarnings("unchecked")
	public HibernateCrudJpaDao() {
		// 这个需要子类中指定T为实际的类才有效
		Type type = this.getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			type = ((ParameterizedType) type).getActualTypeArguments()[0];
			if (type instanceof Class)
				this.entityClass = (Class<T>) type;
		}
	}

	public void setEntityManagerFactory(
			EntityManagerFactory entityManagerFactory) {
		this.jpaTemplate = new JpaTemplate(entityManagerFactory);
	}

	public void setPkName(String pkName) {
		this.pkName = pkName;
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<T> persistentClass) {
		this.entityClass = persistentClass;
	}

	private String getEntityName() {
		return this.entityClass.getSimpleName();
	}

	/**
	 * 返回默认的基于Hibernate实现的查询器
	 * 
	 * @see qc.core.dao.CrudDao#createQuery()
	 */
	public qc.core.query.Query<T> createQuery() {
		return defaultQuery(this.entityClass);
	}

	@SuppressWarnings("unchecked")
	protected qc.core.query.Query<T> defaultQuery(Class<T> persistentClass) {
		if (persistentClass == null)
			return (qc.core.query.Query) new HibernateJpaQuery<T>(
					this.jpaTemplate);
		else
			return (qc.core.query.Query) new HibernateJpaQuery<T>(
					this.jpaTemplate, persistentClass);
	}

	public void delete(Serializable pk) {
		if (pk == null)
			return;

		// 1)hibernate会自动组合find、delete的语句，
		// 最终只执行一句,如：Hibernate: delete from QC_EXAMPLE where ID=?
		// 2)如果要删除的对象为null，会抛异常：nested exception is
		// java.lang.IllegalArgumentException: attempt to create delete event
		// with null entity
		T e = this.jpaTemplate.find(entityClass, pk);
		if (e != null)
			this.jpaTemplate.remove(e);
		//else
		//	throw new QcException("");
	}

	public void delete(Serializable[] pks) {
		if (pks == null || pks.length == 0)
			return;

		for (Serializable pk : pks)
			this.delete(pk);

		// final List<Object> args = new ArrayList<Object>();
		// final StringBuffer hql = new StringBuffer();
		// hql.append("delete " + this.getEntityName() + " _alias");
		// if (pks.length == 1) {
		// hql.append(" where _alias." + pkName + "=?");
		// args.add(pks[0]);
		// } else {
		// int i = 0;
		// hql.append(" where _alias." + pkName + " in (");
		// for (Serializable pk : pks) {
		// hql.append(i == 0 ? "?" : ",?");
		// args.add(pk);
		// i++;
		// }
		// hql.append(")");
		// }
		// this.executeUpdate(hql.toString(), args);
	}

	public T load(Serializable pk) {
		return this.jpaTemplate.find(this.entityClass, pk);
	}

	@SuppressWarnings("unchecked")
	public void save(T entity) {
		if (null != entity) {
			if (entity instanceof qc.core.Entity) {
				if (((qc.core.Entity) entity).isNew()) {
					this.jpaTemplate.persist(entity);
				} else {
					this.jpaTemplate.merge(entity);
				}
			} else {
				this.jpaTemplate.persist(entity);// may be error if had set the
													// id!
			}
		}
	}

	public void save(Collection<T> entities) {
		if (null != entities && !entities.isEmpty()) {
			for (T entity : entities)
				this.jpaTemplate.persist(entity);
		}
	}

	public void update(Serializable pk, Map<String, Object> attributes) {
		if (pk == null || attributes == null || attributes.isEmpty())
			return;
		this.update(new Serializable[] { pk }, attributes);
	}

	public void update(Serializable[] pks, Map<String, Object> attributes) {
		if (pks == null || pks.length == 0 || attributes == null
				|| attributes.isEmpty())
			return;

		final List<Object> args = new ArrayList<Object>();
		final StringBuffer hql = new StringBuffer();
		hql.append("update " + this.getEntityName() + " _alias");

		// set
		int i = 0;
		for (String key : attributes.keySet()) {
			if (i > 0)
				hql.append(",_alias." + key + "=?");
			else
				hql.append(" set _alias." + key + "=?");
			args.add(attributes.get(key));
			i++;
		}

		// pks
		if (pks.length == 1) {
			hql.append(" where _alias." + pkName + "=?");
			args.add(pks[0]);
		} else {
			i = 0;
			hql.append(" where _alias." + pkName + " in (");
			for (Serializable pk : pks) {
				hql.append(i == 0 ? "?" : ",?");
				args.add(pk);
				i++;
			}
			hql.append(")");
		}

		this.executeUpdate(hql.toString(), args);
	}

	@SuppressWarnings("unchecked")
	private void executeUpdate(final String hql, final List<Object> args) {
		if (logger.isDebugEnabled()) {
			logger.debug("hql=" + hql);
			logger.debug("args="
					+ StringUtils.collectionToCommaDelimitedString(args));
		}
		Object o = this.jpaTemplate.execute(new JpaCallback() {
			public Object doInJpa(EntityManager em) throws PersistenceException {
				javax.persistence.Query query = createQuery(em, hql, args
						.toArray());
				jpaTemplate.prepareQuery(query);
				return query.executeUpdate();
			}
		});
		if (logger.isDebugEnabled())
			logger.debug("executeUpdate count=" + o);
	}

	/**
	 * 创建查询对象
	 * 
	 * @param hql
	 *            查询语句
	 * @param args
	 *            查询语句中的参数
	 * @return 构建好的查询对象
	 */
	protected javax.persistence.Query createQuery(EntityManager em, String hql,
			Object[] args) {
		javax.persistence.Query queryObj = em.createQuery(hql);
		if (null != args && args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				queryObj.setParameter(i + 1, args[i]);// jpa索引从1开始
			}
		}
		return queryObj;
	}
}
