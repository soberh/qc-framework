package qc.orm.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import qc.core.Page;
import qc.core.dao.CrudDao;
import qc.core.jdbc.SimpleJdbcInsertEx;
import qc.core.query.condition.impl.EqualsCondition;
import qc.test.Example;

public abstract class AbstractDaoTest extends
		AbstractTransactionalJUnit4SpringContextTests {
	private SimpleJdbcInsert jdbcInsert;
	private static String uuid = UUID.randomUUID().toString();

	public SimpleJdbcInsert getJdbcInsert() {
		if (jdbcInsert == null) {
			jdbcInsert = new SimpleJdbcInsertEx(this.applicationContext
					.getBean("dataSource", DataSource.class)).withTableName(
					"qc_example").usingGeneratedKeyColumns("id");
			// System.out.println("####jdbcInsert=" + jdbcInsert);
		}
		return jdbcInsert;
	}

	private CrudDao<Example> crudDao;

	@Autowired
	public void setCrudDao(CrudDao<Example> crudDao) {
		this.crudDao = crudDao;
	}

	@Test
	@Rollback(true)
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
		Long id1 = insertOne("name");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "newName");
		crudDao.update(id1, map);
		Example entity = crudDao.load(id1);
		Assert.assertNotNull(entity);
		Assert.assertEquals("newName", entity.getName());
	}

	@Test
	public void updateMul() {
		Long id1 = insertOne("name");
		Long id2 = insertOne("name1");

		// update
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "newName");
		crudDao.update(new Long[] { id1, id2 }, map);

		Example entity = crudDao.load(id1);
		Assert.assertNotNull(entity);
		Assert.assertEquals("newName", entity.getName());

		entity = crudDao.load(id2);
		Assert.assertNotNull(entity);
		Assert.assertEquals("newName", entity.getName());
	}

	@Test
	@Rollback(true)
	public void load() {
		Long id1 = insertOne("name");
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
		q.condition(new EqualsCondition("id", id1));
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
		q.condition(new EqualsCondition("id", id1));
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
		q.condition(new EqualsCondition("id", id1));
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
		Map<String, Object> parameters = new HashMap<String, Object>(1);
		parameters.put("name", name);
		Number newId = this.getJdbcInsert().executeAndReturnKey(parameters);
		Long id = new Long(newId.longValue());
		Assert.assertTrue(id > 0);
		// System.out.println(id);
		return id;
	}

	// 在oracle中必须指定id的值为hibernate_sequence.nextval，mysql中只需
	// qc_example(name,code)即可
	// private String insertSqlTpl =
	// "insert into qc_example(name,code) values(V)";

	// // 往数据库插入一条测试数据,返回插入数据的id值
	// private long insertOneByJdbc(String name) {
	// final String sql = insertSqlTpl
	// .replaceAll("V", "'" + name + "','code'");
	// // This support is part of the JDBC 3.0 standard;
	// KeyHolder keyHolder = new GeneratedKeyHolder();
	//
	// simpleJdbcTemplate.getJdbcOperations().update(
	// new PreparedStatementCreator() {
	// public PreparedStatement createPreparedStatement(
	// Connection conn) throws SQLException {
	// // PreparedStatement ps = conn.prepareStatement(sql,new
	// // String[]{"id"});-->oracle need it
	// PreparedStatement ps = conn.prepareStatement(sql);
	// return ps;
	// }
	// }, keyHolder);
	// return keyHolder.getKey().longValue();
	// }
}
