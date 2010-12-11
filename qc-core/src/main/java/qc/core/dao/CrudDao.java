/**
 * 
 */
package qc.core.dao;

import qc.core.CrudOperations;

/**
 * CrudDao接口
 * 
 * @author dragon
 * 
 * @param <T>
 *            对象类型
 */
public interface CrudDao<T extends Object> extends CrudOperations<T> {
}
