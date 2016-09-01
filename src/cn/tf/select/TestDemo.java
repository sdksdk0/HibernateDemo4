package cn.tf.select;

import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import cn.tf.init.Customer;
import cn.tf.init.Order;



public class TestDemo {
	private SessionFactory factory = new Configuration()
	.configure()
	.addClass(Order.class)
	.addClass(Customer.class)
	.buildSessionFactory();
	
	
/*	//get,立即检索
	@Test
	public void test1(){
		Session session = factory.openSession();
		session.beginTransaction();
		
		Customer customer=(Customer) session.get(Customer.class, 1);
		System.out.println(customer);
		
		session.getTransaction().commit();
		session.close();
	}
	
	@Test
	public void test2(){
		Session session = factory.openSession();
		session.beginTransaction();
		
		Customer customer=(Customer) session.load(Customer.class, 1);
		System.out.println(customer.getCid());
		
		session.getTransaction().commit();
		session.close();
	}*/
	
	//关联，一对多，多对多
/*	@Test
	public void test3(){
		Session session = factory.openSession();
		session.beginTransaction();
		
		//查询客户
		Customer customer=(Customer) session.load(Customer.class, 1);
		System.out.println(customer.getName());
		Set<Order> orderSet=customer.getOrderSet();
		
		//查询客户订单数
		System.out.println(orderSet.size());
		
		//订单详情
		for (Order order : orderSet) {
			System.out.println(order.getPrice());
		}
		
		
		session.getTransaction().commit();
		session.close();
	}*/
	
	
	/**
	 * #3 关联级别：一对多、多对多
	 * 	Customer.hbm.xml 配置  
	 * 		<set name="orderSet" fetch="subselect" lazy="false">
	 * 
	 */
	@Test
	public void demo04(){
		Session session = factory.openSession();
		session.beginTransaction();
		
		//0 必须使用query查询
		List<Customer> allCustomer = session.createQuery("from Customer").list();
		
		//1 查询客户
		Customer customer = allCustomer.get(0); // (Customer) session.get(Customer.class, 1);
		System.out.println(customer.getName());
		
		Set<Order> orderSet = customer.getOrderSet(); 
		//2 统计客户订单数
		System.out.println(orderSet.size());
		
		//3 订单详情
		for (Order order : orderSet) {
			System.out.println(order.getPrice());
		}
		
		
		
		session.getTransaction().commit();
		session.close();
		
	}
	
	

}
