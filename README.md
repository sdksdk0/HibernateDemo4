# HibernateDemo4
Hibernate基础，继承映射、检索方式（通过使用HQL,SQL,QBC分别进行分页，连接查询，离线查询，聚合函数等）

##组件映射

通过面向对象角度，使用设计模式，将数据都抽取到一个对象中，将多个对象组合在一起，达到重复利用的目的。

必须确定组合javabean类型，每一个对象属性必须在表中都存在独有列名。

	<class  name="cn.tf.component.Person"   table="t_person">
		<id  name="pid">
			<generator class="native"></generator>
		</id>
		<property name="name"></property>

		<component name="homeAddress"  class="cn.tf.component.Address">
				<property name="addr"  column="homeAddr"></property>
				<property name="tel"  column="homeTel"></property>
		</component>
		
		<component name="companyAddress"  class="cn.tf.component.Address">
				<property name="addr"  column="companyAddr"></property>
				<property name="tel"  column="companyTel"></property>
		</component>


##继承映射

继承方式1：sub-class
所有内容保存一张表，给表提供标识字段，每一个子类都具有独有的字段。

继承方式1：

    
    <class  name="cn.tf.inherit.subclass.Employee"   table="t_employee"   discriminator-value="员工">
		<id  name="eid">
			<generator class="native"></generator>
		</id>
		
		<discriminator column="etemp"></discriminator>

		<property name="name"></property>

		<subclass  name="cn.tf.inherit.subclass.SalaryEmployee"  discriminator-value="正式员工">
			<property name="salary"></property>
		</subclass>
		<subclass  name="cn.tf.inherit.subclass.HourEmployee"  discriminator-value="小时工">
			<property name="rate"></property>
		</subclass>
		

继承方式2：

	<joined-subclass  name="cn.tf.inherit.joinedclass.HourEmployee"  table="j_hour">
		<key column="hid"></key>
		<property name="rate"></property>
	</joined-subclass>
	<joined-subclass  name="cn.tf.inherit.joinedclass.SalaryEmployee"  table="j_salary">
		<key column="sid"></key>
		<property name="salary"></property>
	</joined-subclass>	


继承方式3：
将生成多张表，彼此之间没有关系，但主键值逐渐增强，需要hiberate自动维护三张表之间的主键唯一，存在并发问题。

	<union-subclass name="cn.tf.inherit.unionclass.HourEmployee"  table="u_hour">
		<property name="rate"></property>
	</union-subclass>
	<union-subclass name="cn.tf.inherit.unionclass.SalaryEmployee"  table="u_salary">
		<property name="salary"></property>
	</union-subclass>	
		



##抓取策略

###检索方式
立即检索：在执行查询之后，立即执行select语句进行查询

延迟检索：在执行查询方法之后，没有进行查询，底层生成代理对象呢，直到需要相应的数据，在进行查询。
除OID之外的值，关联的数据

###检索类型
类别级检索：当前对象所有属性值。，查询当前类的所有内容，只查询一次，优化指查询时机优化。


关联级检索：当前对象关联对象数据。


多对一，一对一：共同特点都拥有一方

一对多、多对多

    <set name="orderSet"   fetch="join"  lazy="true">

fetch用来确定hibernate生成的sql的样式，lazy表示制定sql的时间。

fetch="join" ，lazy无效，hibernate 将使用“迫切左外连接”，底层执行sql语句就是“左外连接”，只执行一条select，将当前对象及关联对象数据一次性查询出来。

fetch="select" ，默认值，执行多条select语句

lazy="false" 立即执行，在执行get方法时，立即执行多条select语句。

lazy="true" 延迟执行，在执行get方法时先查询客户Customer，直到使用Order数据时才查询Order

lazy="extra" 极其懒惰，在执行get方法时先查询客户Customer（select t_customer），如果需要order 订单数，将执行聚合函数只查询数量（select count() t_order），如果需要详情再查询详情(select t_order))。
fetch="subselect" ，采用子查询

lazy 取值与 fetch="select" 相同。
注意：必须使用Query进行查询。


###检索方式总结
1、通过OID检索：get()立即查询，如果没有就返回null;load()默认延迟检查，如果没有抛出异常。使用以上两个方法进行查询，结果都是持久态对象，持久态对象就在以及缓存中。

2、对象导航图：通过持久对象自动获得关联对象。例如customer.getOrderSet().size()

3、SQL，session.createSQLQuery("sql语句")

4,QBC,hibernate提供的纯面向对象查询语句。

5，HQL， 提供面向对象的查询语句。
HQL使用对象和对象属性，sql语句使用表名和字段名称，对象和属性区分大小写，表名和字段不区分大小写。

###查询所有

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

###根据ID查询

	//hql
		//Customer customer=(Customer) session.createQuery("from Customer c where c.cid=1").uniqueResult();
		
		//sql
		//Customer customer=(Customer) session.createSQLQuery("select * from t_customer c where  c.cid=1").addEntity(Customer.class).uniqueResult();
		
		//QBC
		Customer customer=(Customer) session.createCriteria(Customer.class)
				.add(Restrictions.eq("cid", 1))
				.uniqueResult();


###排序

	//hql
		//List<Customer> allCustomer=(List<Customer>) session.createQuery("from Customer c order by  c.cid ").list();
		
		//sql
		//List<Customer> allCustomer=(List<Customer>) session.createSQLQuery("select * from t_customer order by cid desc").addEntity(Customer.class).list();
		
		//QBC
		List<Customer> allCustomer=(List<Customer>)  session.createCriteria(Customer.class).addOrder(org.hibernate.criterion.Order.asc("cid")).list();
	
		for (Customer customer : allCustomer) {
			System.out.println(customer);
		}


###查询部分内容（投影）

hql,默认查询部分将查询结果封装到object数组中，需要封装到指定的对象中，投影查询的结果是脱管态 的。
		
	List<Customer> allCustomer=(List<Customer>) session.createQuery(" select new Customer(cid,name)  from Customer  ").list();
		
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
	
###分页查询

	//1、HQL
		//List<Customer>  allCustomer=session.createQuery("from Customer").setFirstResult(0).setMaxResults(2).list();

		//sql,必须将查询结果封装到相应的对象中
		//List<Customer>  allCustomer=session.createSQLQuery("select * from t_customer").addEntity(Customer.class).setFirstResult(0).setMaxResults(2).list();
		
		//QBC
		List<Customer>  allCustomer=session.createCriteria(Customer.class).setFirstResult(0).setMaxResults(2).list();;
	

###绑定

 占位符：
	#属性=?
	 		从0开始,setXXX(index,oid);第一个参数表述索引号，从0开始，第二个表示oid的值,也可以使用.setParameter(0, 1)
别名：

	 * 	属性=:别名
	 * 		第一个参数是别名，一般建议使用属性名称


    Customer  customer=(Customer) session.createQuery("from Customer  c where c.cid= ? ")
				//.setInteger(0, 1)  
				.setParameter(0, 1)
				.uniqueResult();

		Customer  customer=(Customer) session.createQuery("from Customer  c where c.cid=:cid")
				.setInteger("cid", 1)  
				.uniqueResult();



###聚合函数

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



###连接查询

1. 内连接：[inner] join
1. 左外连接： left [outer] join
1. 右外连接： right [outer] join
1. 迫切左外连接： left [outer] join fetch
1. 迫切内连接： [inner]join fetch



左外连接,将每一条查询结果封装到Customer 和Order对象，然后创建 new Object[2]{c,o} 。将所有的数组 存放到另一个数组返回

			List<Customer> allCustomer=session.createQuery("from  Customer  c left outer join  c.orderSet ").list();
			
迫切左外连接，将所有的查询结果封装Customer和Order对象，然后将Order 添加到 Customer(customer.getOrderSet().add(order)) ,最后返回Customer对象集合

			List<Customer> allCustomer=session.createQuery("select distinct c from Customer  c left outer join fetch  c.orderSet ").list();
	
###命名查询

将HQL语句存放到配置文件中，之后不需要修改java源码，服务器tomcat重启即可。

全局：可以直接获得，在。hbm.xml文件中配置。与class同级之后


    <!-- 配置全局hql -->
    <class></class>
    	<query name="findAllCustomer">from Customer</query>
	
使用：

    List<Customer>  allCustomer=(List<Customer> )session.getNamedQuery("findAllCustomer").list();
		

局部：必须通过包名获得
    
    <class><!-- 局部 -->
    		<query name="findAllCustomer2">from Customer</query></class>

使用：

    List<Customer>  allCustomer=(List<Customer> )session.getNamedQuery("cn.tf.init.Customer.findAllCustomer2").list();
    	

