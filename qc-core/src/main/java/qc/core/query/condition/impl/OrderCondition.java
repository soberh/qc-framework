/**
 * 
 */
package qc.core.query.condition.impl;

import java.util.List;
import java.util.regex.Pattern;

import qc.core.exception.QcException;
import qc.core.query.condition.Condition;
import qc.core.query.condition.Direction;

/**
 * 排序条件
 * 
 * @author dragon
 * 
 */
public class OrderCondition implements Condition {
	private String orderBy;

	//internal use
	protected OrderCondition() {
	}

	public OrderCondition(String name, Direction direction) {
		if (name == null)
			throw new QcException("name can not be null.");
		this.add(name, direction);
	}

	public void add(OrderCondition orderCondition) {
		if (orderCondition == null)
			throw new QcException("orderCondition can not be null.");
		String _orderBy = orderCondition.getExpression();
		_orderBy = Pattern.compile("order\\s*by\\s", Pattern.CASE_INSENSITIVE)
				.matcher(_orderBy).replaceAll("");
		//System.out.println(_orderBy);
		if (_orderBy != null && _orderBy.length() > 0) {
			if (this.orderBy == null) {
				this.orderBy = "";
			} else {
				this.orderBy = this.orderBy + ",";
			}
			this.orderBy = this.orderBy + _orderBy;
		}
		//System.out.println("this.orderBy=" + this.orderBy);
	}

	public void add(String name, Direction direction) {
		if (this.orderBy == null) {
			this.orderBy = "";
		} else {
			this.orderBy = this.orderBy + ",";
		}
		this.orderBy = this.orderBy + name + " " + direction.toSymbol();
	}

	public String getExpression() {
		return this.orderBy == null ? "" : this.orderBy;
	}

	public List<Object> getValues() {
		return null;
	}
}
