package cn.tf.hql;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.junit.Test;

import cn.tf.init.Customer;
import cn.tf.init.Order;



public class TestDemo {
	private SessionFactory factory = new Configuration()
	.configure()
	.addClass(Order.class)
	.addClass(Customer.class)
	.buildSessionFactory();
	
	
	//查询所有
	/*@Test
	public void test1(){
		Session session = factory.openSession();
		session.beginTransaction();
		
		//1、HQL
		//Query query=session.createQuery("from Customer");
		
		//使用全限定名
		//Query query=session.createQuery("from cn.tf.init.Customer");
		
		//别名,不能使用*号
		//Query query=session.createQuery("select c  from Customer as c");
		
		
		//sql,必须将查询结果封装到相应的对象中
		SQLQuery sqlQuery=session.createSQLQuery("select * from t_customer");
		sqlQuery.addEntity(Customer.class);
		List<Customer>  allCustomer=sqlQuery.list();
		
		//QBC
		Criteria criteria=session.createCriteria(Customer.class);
		List<Customer> allCustomer=criteria.list();
		
		//List<Customer>  allCustomer=query.list();
		
		//输出
		for (Customer customer : allCustomer) {
			System.out.println(customer);
		}
		
		
		session.getTransaction().commit();
		session.close();
	}
	*/
	
	//通过id查询
	@Test
	public void test2(){
		Session session = factory.openSession();
		session.beginTransaction();
		//hql
		//Customer customer=(Customer) session.createQuery("from Customer c where c.cid=1").uniqueResult();
		
		//sql
		//Customer customer=(Customer) session.createSQLQuery("select * from t_customer c where  c.cid=1").addEntity(Customer.class).uniqueResult();
		
		//QBC
		Customer customer=(Customer) session.createCriteria(Customer.class)
				.add(Restrictions.eq("cid", 1))
				.uniqueResult();
	
		System.out.println(customer);
		
		session.getTransaction().commit();
		session.close();
	}
	
	//排序
	@Test
	public void test3(){
		Session session = factory.openSession();
		session.beginTransaction();
		//hql
		//List<Customer> allCustomer=(List<Customer>) session.createQuery("from Customer c order by  c.cid ").list();
		
		//sql
		//List<Customer> allCustomer=(List<Customer>) session.createSQLQuery("select * from t_customer order by cid desc").addEntity(Customer.class).list();
		
		//QBC
		List<Customer> allCustomer=(List<Customer>)  session.createCriteria(Customer.class).addOrder(org.hibernate.criterion.Order.asc("cid")).list();
	
		for (Customer customer : allCustomer) {
			System.out.println(customer);
		}
		
		session.getTransaction().commit();
		session.close();
	}
	
	//投影
	@Test
	public void test4(){
		Session session = factory.openSession();
		session.beginTransaction();
		//hql,默认查询部分将查询结果封装到object数组中，需要封装到指定的对象中
		//投影查询的结果是脱管态 的
		//List<Customer> allCustomer=(List<Customer>) session.createQuery(" select new Customer(cid,name)  from Customer  ").list();
		
		//sql
		//List<Customer> allCustomer=(List<Customer>) session.createSQLQuery("select cid,name from t_customer  ").addEntity(Customer.class).list();
		
		//QBC
		List<Customer> allCustomer = session.createCriteria(Customer.class)
				.setProjection(Projections.projectionList()
						.add(Projections.alias(Projections.property("cid"), "cid"))
						.add(Projections.alias(Projections.property("name"), "name"))
						)
						.setResultTransformer( new AliasToBeanResultTransformer(Customer.class))
						.list();
	
		for (Customer customer : allCustomer) {
			System.out.println(customer+" , "+ customer.getOrderSet().size());
		}
		
		session.getTransaction().commit();
		session.close();
	}
	
	
	//分页查询,setFirstResult(),setMaxResult()
	@Test
	public void test5(){
		Session session = factory.openSession();
		session.beginTransaction();
		
		//1、HQL
		//List<Customer>  allCustomer=session.createQuery("from Customer").setFirstResult(0).setMaxResults(2).list();

		//sql,必须将查询结果封装到相应的对象中
		//List<Customer>  allCustomer=session.createSQLQuery("select * from t_customer ").addEntity(Customer.class).setFirstResult(0).setMaxResults(2).list();
		
		//QBC
		List<Customer>  allCustomer=session.createCriteria(Customer.class).setFirstResult(0).setMaxResults(2).list();;
		
		
		//输出
		for (Customer customer : allCustomer) {
			System.out.println(customer);
		}
		
		
		session.getTransaction().commit();
		session.close();
	}
	
	//绑定参数
	/**
	 * 占位符：#属性=?
	 * 		从0开始,setXXX(index,oid);第一个参数表述索引号，从0开始，第二个表示oid的值,也可以使用.setParameter(0, 1)
	 * 别名：
	 * 	属性=:别名
	 * 		第一个参数是别名，一般建议使用属性名称
	 * 
	 */
	
	/*@Test
	public void test6(){
		Session session = factory.openSession();
		session.beginTransaction();
		
		//1、HQL
		Customer  customer=(Customer) session.createQuery("from Customer  c where c.cid= ? ")
				//.setInteger(0, 1)  
				.setParameter(0, 1)
				.uniqueResult();

		Customer  customer=(Customer) session.createQuery("from Customer  c where c.cid=:cid")
				.setInteger("cid", 1)  
				.uniqueResult();
		
		//输出
		System.out.println(customer);
		
		
		session.getTransaction().commit();
		session.close();
	}*/
	 
	
	//聚合查询
	@Test
	public void test7(){
		Session session = factory.openSession();
		session.beginTransaction();
		
		//Object obj=session.createQuery("select count(*) from Customer").uniqueResult();
		
		//Object obj=session.createQuery("select count(*) from Customer").uniqueResult();
		
		Long  numLong=(Long) session.createQuery("select count(*) from Customer").uniqueResult();
		int num=numLong.intValue();
		//输出

		//2 sql
		//BigInteger numObj = (BigInteger) session.createSQLQuery("select count(cid) from t_customer").uniqueResult();
				
		//3 qbc
		Long numObj = (Long)session.createCriteria(Customer.class)
					.setProjection(
							Projections.projectionList()
							.add(Projections.rowCount())
						)
					.uniqueResult();

		System.out.println(num);
		
		session.getTransaction().commit();
		session.close();
	}
	
	
	//连接查询
		@Test
		public void test8(){
			Session session = factory.openSession();
			session.beginTransaction();
			
			//左外连接,将每一条查询结果封装到Customer 和Order对象，然后创建 new Object[2]{c,o} 。将所有的数组 存放到另一个数组返回
			//List<Customer> allCustomer=session.createQuery("from  Customer  c left outer join  c.orderSet ").list();
			
			//迫切左外连接，将所有的查询结果封装Customer和Order对象，然后将Order 添加到 Customer(customer.getOrderSet().add(order)) ,最后返回Customer对象集合
			List<Customer> allCustomer=session.createQuery("  select distinct c from Customer  c left outer join fetch  c.orderSet ").list();
			
			//输出
			for (Customer customer : allCustomer) {
				System.out.println(customer);
			}
			
			session.getTransaction().commit();
			session.close();
		}
		
	
	//命名查询
	@Test
	public void test9(){
		Session session = factory.openSession();
		session.beginTransaction();
		//全局
		@SuppressWarnings("unchecked")
		List<Customer>  allCustomer=(List<Customer> )session.getNamedQuery("findAllCustomer").list();
		
		
		//局部
		//List<Customer>  allCustomer=(List<Customer> )session.getNamedQuery("cn.tf.init.Customer.findAllCustomer2").list();
		
		
		
		for (Customer customer : allCustomer) {
			System.out.println(customer);
		}
		
		
		session.getTransaction().commit();
		session.close();
	}
		
	//离线查询
	@Test
	public void test10(){
		Session session = factory.openSession();
		session.beginTransaction();
		//离线
		DetachedCriteria detachedcriteria=DetachedCriteria.forClass(Customer.class)
				.add(Restrictions.eq("cid", 1));
		
		
		Criteria criteria=detachedcriteria.getExecutableCriteria(session);
		List<Customer> allCustomer=criteria.list();
		for (Customer customer : allCustomer) {
			System.out.println(customer);
		}
		
		
		session.getTransaction().commit();
		session.close();
	}
	
	
		
}
