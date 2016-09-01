package cn.tf.init;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Test;



public class TestDemo {
	private SessionFactory factory = new Configuration()
	.configure()
	.addClass(Order.class)
	.addClass(Customer.class)
	.buildSessionFactory();
	
	
	
	@Test
	public void test1(){
		Session session = factory.openSession();
		session.beginTransaction();
		
		Customer customer=new Customer();
		customer.setName("王武");
		
		for(int i=0;i<9;i++){
			Order order=new Order();
			order.setPrice(i+"");
			order.setCustomer(customer);
			session.save(order);
			
			
		}
		session.save(customer);
		
		session.getTransaction().commit();
		session.close();
	}
	

   

}
