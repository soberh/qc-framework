/**
 * 
 */
package qc.identity;


/**
 * Actor之间的关联关系 如： 1) 用户所隶属的单位或部门 2) 用户所在岗位 3) 组织的负责人、正职、副职、归档人等 4) 不同组织间的业务关系
 * @author  dragon
 */
public interface Membership {
	/**
	 * @return   返回关联关系中的主控方，如岗位与用户关系中的岗位
	 * @uml.property  name="master"
	 * @uml.associationEnd  
	 */
	Actor getMaster();
	
	/**
	 * 设置关联关系中的主控方
	 * @param  master
	 * @uml.property  name="master"
	 */
	void setMaster(Actor master);
	
	/**
	 * @return   返回关联关系中的从属方，如岗位与用户关系中的用户
	 * @uml.property  name="follower"
	 * @uml.associationEnd  
	 */
	Actor getFollower();
	
	/**
	 * 设置关联关系中的从属方
	 * @param  follower
	 * @uml.property  name="follower"
	 */
	void setFollower(Actor follower	);
	
	/**
	 * @return  返回关联类型
	 * @uml.property  name="type"
	 */
	String getType();
	
	/**
	 * 设置关联类型
	 * @param  string
	 * @uml.property  name="type"
	 */
	void setType(String string);
	
	/**
	 * @return  返回多个从属方之间的排序号
	 * @uml.property  name="order"
	 */
	String getOrder();
	
	/**
	 * 设置多个从属方之间的排序号
	 * @param  order
	 * @uml.property  name="order"
	 */
	void setOrder(String order);
}
