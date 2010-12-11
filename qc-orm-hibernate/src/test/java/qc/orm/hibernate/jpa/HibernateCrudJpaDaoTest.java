package qc.orm.hibernate.jpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import qc.core.Page;
import qc.core.dao.CrudDao;
import qc.core.jdbc.SimpleJdbcInsertEx;
import qc.core.query.condition.impl.EqualsCondition;
import qc.test.Example;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration
public class HibernateCrudJpaDaoTest {
	protected SimpleJdbcTemplate simpleJdbcTemplate;
	private SimpleJdbcInsert jdbcInsert;
	private static String uuid = UUID.randomUUID().toString();

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);
		this.jdbcInsert = new SimpleJdbcInsertEx(dataSource).withTableName(
				"qc_example").usingGeneratedKeyColumns("id");
	}

	public SimpleJdbcInsert getJdbcInsert() {
		return jdbcInsert;
	}

	private CrudDao<Example> crudDao;

	@Autowired
	public void setCrudDao(CrudDao<Example> crudDao) {
		this.crudDao = crudDao;
	}

	// 记录在事务开始前预插入的测试数据的id
	private List<Long> ids = new ArrayList<Long>();

	@BeforeTransaction
	public void beforeTransaction() {
		Map<String, Object> parameters = new HashMap<String, Object>(1);
		parameters.put("name", "name");
		Number newId = this.getJdbcInsert().executeAndReturnKey(parameters);
		Long id = new Long(newId.longValue());
		Assert.assertTrue(id > 0);
		ids.add(id);
		// System.out.println(id);
	}

	// 删除插入的测试数据
	@AfterTransaction
	public void afterTransaction() {
		if (ids.isEmpty())
			return;
		String sql = "delete from qc_example where id in(";
		for (int i = 0; i < ids.size(); i++) {
			sql += (i == 0 ? "?" : ",?");
		}
		sql += ")";
		this.simpleJdbcTemplate.update(sql, ids.toArray());
	}

	@Test
	public void save() {
		Example entity = new Example("test1");
		crudDao.save(entity);
		Assert.assertTrue(entity.getId() > 0);

		// load
		entity = crudDao.load(entity.getId());
		Assert.assertNotNull(entity);
		Assert.assertEquals("test1", entity.getName());
	}

	@Test
	public void saveMul() {
		List<Example> entities = new ArrayList<Example>();
		entities.add(new Example("test1"));
		entities.add(new Example("test2"));
		crudDao.save(entities);
		Assert.assertTrue(entities.get(0).getId() > 0);
		Assert.assertTrue(entities.get(1).getId() > 0);
		Assert.assertTrue(entities.get(0).getId() != entities.get(1).getId());
	}

	@Test
	public void delete() {
		Long id1 = insertOne("name");
		crudDao.delete(id1);
		Example entity = crudDao.load(id1);
		Assert.assertNull(entity);
	}

	@Test
	public void deleteMul() {
		Long id1 = insertOne("name");
		Long id2 = insertOne("name1");
		crudDao.delete(new Long[] { id1, id2 });
		Example entity = crudDao.load(id1);
		Assert.assertNull(entity);
		entity = crudDao.load(id2);
		Assert.assertNull(entity);
	}

	@Test
	public void update() {
		Long id1 = ids.get(0);// insertOne("name");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "newName");
		crudDao.update(id1, map);
		Example entity = crudDao.load(id1);
		Assert.assertNotNull(entity);
		Assert.assertEquals("newName", entity.getName());
	}

	@Test
	public void load() {
		Long id1 = ids.get(0);// insertOne("name");
		Example entity = crudDao.load(id1);
		Assert.assertNotNull(entity);
		Assert.assertEquals("name", entity.getName());
	}

	@Test
	public void query_count() {
		// 插入0条
		qc.core.query.Query<Example> q = crudDao.createQuery();
		q.condition(new EqualsCondition("id", new Long(0)));
		Assert.assertNotNull(q);
		Assert.assertEquals(0, q.count());

		// 插入1条
		Long id1 = insertOne("name0");
		Assert.assertTrue(id1 > 0);
		q = crudDao.createQuery();
		q.condition(new EqualsCondition("id", new Long(id1)));
		Assert.assertEquals(1, q.count());

		// 插入10条
		for (int i = 0; i < 10; i++)
			insertOne(uuid);
		q.condition(new EqualsCondition("name", uuid));
		Assert.assertEquals(10, q.count());
	}

	@Test
	public void query_singleResult() {
		Long id1 = insertOne("name");
		Assert.assertTrue(id1 > 0);
		qc.core.query.Query<Example> q = crudDao.createQuery();
		q.condition(new EqualsCondition("id", new Long(id1)));
		Assert.assertNotNull(q);
		Example e = q.singleResult();
		Assert.assertNotNull(e);
		Assert.assertEquals(id1, e.getId());
		Assert.assertEquals("name", e.getName());
	}

	@Test
	public void query_list() {
		// 插入0条
		qc.core.query.Query<Example> q = crudDao.createQuery();
		q.condition(new EqualsCondition("id", new Long(0)));
		Assert.assertNotNull(q);
		List<Example> list = q.list();
		Assert.assertNotNull(list);
		Assert.assertTrue(list.size() == 0);

		// 插入1条
		Long id1 = insertOne("name0");
		Assert.assertTrue(id1 > 0);
		q = crudDao.createQuery();
		q.condition(new EqualsCondition("id", new Long(id1)));
		list = q.list();
		Assert.assertNotNull(list);
		Assert.assertTrue(list.size() == 1);

		// 插入10条
		for (int i = 0; i < 10; i++)
			insertOne(uuid);
		q.condition(new EqualsCondition("name", uuid));
		list = q.list();
		Assert.assertNotNull(list);
		Assert.assertTrue(list.size() == 10);
	}

	@Test
	public void query_page() {
		// 插入0条
		qc.core.query.Query<Example> q = crudDao.createQuery();
		q.condition(new EqualsCondition("id", new Long(0)));
		Assert.assertNotNull(q);
		Page<Example> page = q.page(1, 100);
		Assert.assertNotNull(page);
		Assert.assertTrue(page.getList() == null || page.getList().isEmpty());
		Assert.assertEquals(1,page.getPageNo());
		Assert.assertEquals(100,page.getPageSize());
		Assert.assertEquals(0,page.getPageCount());
		Assert.assertEquals(0,page.getTotalCount());
		Assert.assertEquals(0,page.getFirstResult());

		// 插入1条
		Long id1 = insertOne("name0");
		Assert.assertTrue(id1 > 0);
		q = crudDao.createQuery();
		q.condition(new EqualsCondition("id", id1));
		page = q.page(1, 100);
		Assert.assertTrue(page.getList() != null && !page.getList().isEmpty());
		Assert.assertEquals(1,page.getPageNo());
		Assert.assertEquals(100,page.getPageSize());
		Assert.assertEquals(1,page.getPageCount());
		Assert.assertEquals(1,page.getTotalCount());
		Assert.assertEquals(0,page.getFirstResult());

		// 插入10条
		for (int i = 0; i < 10; i++)
			insertOne(uuid);
		q.condition(new EqualsCondition("name", uuid));
		page = q.page(1, 100);
		Assert.assertNotNull(page.getList());
		Assert.assertEquals(10,page.getList().size());
		Assert.assertEquals(1,page.getPageNo());
		Assert.assertEquals(100,page.getPageSize());
		Assert.assertEquals(1,page.getPageCount());
		Assert.assertEquals(10,page.getTotalCount());
		Assert.assertEquals(0,page.getFirstResult());

		// 第1页
		page = q.page(1, 5);
		Assert.assertEquals(5,page.getList().size());
		Assert.assertEquals(1,page.getPageNo());
		Assert.assertEquals(5,page.getPageSize());
		Assert.assertEquals(2,page.getPageCount());
		Assert.assertEquals(10,page.getTotalCount());
		Assert.assertEquals(0,page.getFirstResult());

		// 第2页
		page = q.page(2, 3);
		Assert.assertEquals(3,page.getList().size());
		Assert.assertEquals(2,page.getPageNo());
		Assert.assertEquals(3,page.getPageSize());
		Assert.assertEquals(4,page.getPageCount());
		Assert.assertEquals(10,page.getTotalCount());
		Assert.assertEquals(3,page.getFirstResult());
	}

	/**
	 * 向数据库中插入一条新数据
	 * 
	 * @return 返回主键的值
	 */
	private Long insertOne(String name) {
		Example entity = new Example(name);
		crudDao.save(entity);
		Assert.assertFalse(entity.isNew());
		return entity.getId();
	}
}
