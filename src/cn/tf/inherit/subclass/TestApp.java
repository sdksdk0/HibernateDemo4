package cn.tf.inherit.subclass;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

public class TestApp {
	
	private SessionFactory factory = new Configuration()
			.configure()
			.addClass(Employee.class)
			.buildSessionFactory();
	
	@Test
	public void demo01(){
		
		Session session = factory.openSession();
		session.beginTransaction();
		
		/*Employee employee = new Employee();
		employee.setName("张三");*/
		
		HourEmployee employee = new HourEmployee();
		employee.setName("李四");
		employee.setRate(100);
		
		session.save(employee);
		
		session.getTransaction().commit();
		session.close();
		
	}

}
