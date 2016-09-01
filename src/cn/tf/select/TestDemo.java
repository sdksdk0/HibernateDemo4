package cn.tf.select;

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
	
	
	//get,立即检索
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
	}
   

}
