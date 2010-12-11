/**
 * 
 */
package qc.identity;


/**
 * 用户接口，代表人的抽象标识 标准接口中只定义标识、姓名、邮箱、电话属性，其余的需通过 setAttribute(String key, String value)和getAttribute(String key)方法扩展
 * @author  dragon
 */
public interface User extends Actor{
	/**
	 * @return  用户的姓氏
	 * @uml.property  name="firstName"
	 */
	String getFirstName();
	/**
	 * @param firstName
	 * @uml.property  name="firstName"
	 */
	void setFirstName(String firstName);
	
	/**
	 * @return  用户的名称
	 * @uml.property  name="lastName"
	 */
	String getLastName();
	/**
	 * @param  lastName
	 * @uml.property  name="lastName"
	 */
	void setLastName(String lastName);
	
	/**
	 * @return  用户的联系邮箱
	 * @uml.property  name="email"
	 */
	String getEmail();
	/**
	 * @param email
	 * @uml.property  name="email"
	 */
	void setEmail(String email);
	
	/**
	 * @return  用户的联系电话
	 * @uml.property  name="phone"
	 */
	String getPhone();
	/**
	 * @param phone
	 * @uml.property  name="phone"
	 */
	void setPhone(String phone);
	
	/**
	 * 获取指定扩展属性的值
	 * @param key 属性名称
	 * @return
	 */
	Object getAttribute(String key);
	
	/**
	 * 设置或添加指定的扩展属性
	 * @param key 属性名称	
	 * @param value 属性值
	 */
	void setAttribute(String key, Object value);
}
