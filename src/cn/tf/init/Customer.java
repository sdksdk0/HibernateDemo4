package cn.tf.init;

import java.util.HashSet;
import java.util.Set;

public class Customer {
	
	private Integer cid;
	private String name;
	
	private Set<Order> orderSet = new HashSet<Order>();
	
	
	/**
	 * 必须提供无参构造（默认构造）
	 */
	public Customer() {
		super();
	}
	
	public Customer(Integer cid, String name) {
		super();
		this.cid = cid;
		this.name = name;
	}



	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Order> getOrderSet() {
		return orderSet;
	}

	public void setOrderSet(Set<Order> orderSet) {
		this.orderSet = orderSet;
	}

	@Override
	public String toString() {
		return "Customer [cid=" + cid + ", name=" + name + "]";
	}
	

}
