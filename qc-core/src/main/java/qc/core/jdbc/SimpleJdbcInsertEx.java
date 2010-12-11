/**
 * 
 */
package qc.core.jdbc;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * 扩展spring的jdbc插入器，使其支持基于通过sequence生成主键的数据库，如oracle。 
 * 需要额外设置属性sequence为对应的序列名
 * 
 * @author dragon
 * 
 */
public class SimpleJdbcInsertEx extends SimpleJdbcInsert {

	public SimpleJdbcInsertEx(DataSource dataSource) {
		super(dataSource);
	}

	public SimpleJdbcInsertEx(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate);
	}

	//private String insertString;

	@Override
	public String getInsertString() {
		//if (insertString != null)
		//	return insertString;// 缓存

		// 父类默认生成的语句格式为：
		// "INSERT INTO [SchemaName].[TableName] ([columnName1], [columnName2], ...) VALUES(?, ?, ...)"
		String insertString = super.getInsertString();

		if (this.isUseSequence() && this.getGeneratedKeyNames().length > 0) {
			// 暂时仅支持单一数字自增主键
			String pkKey = this.getGeneratedKeyNames()[0];

			insertString = insertString.replaceFirst(" \\(", " (" + pkKey
					+ ", ");
			insertString = insertString.replaceFirst(" VALUES\\(", " VALUES("
					+ getSequence() + ".nextval, ");
			if (logger.isDebugEnabled()) {
				logger.debug("rebuild Insert string is [" + insertString + "]");
			}
		}
		return insertString;
	}

	private String sequence;

	public boolean isUseSequence() {
		return !(sequence == null || sequence.length() == 0);
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
}
