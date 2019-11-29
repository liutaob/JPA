package com.atguigu.jpa.test;

import com.atguigu.jpa.helloworld.*;
import org.hibernate.ejb.QueryHints;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.*;
import javax.sound.midi.Soundbank;
import java.net.Socket;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JPATest {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction transaction;

    @Before
    public void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpa-1");
        entityManager = entityManagerFactory.createEntityManager();
        transaction = entityManager.getTransaction();
        transaction.begin();
    }

    @After
    public void destroy() {
        transaction.commit();
        entityManager.close();
        entityManagerFactory.close();
    }

    //可以使用 JPQL 完成 UPDATE 和 DELETE 操作.//不设置条件就全部更新了
    @Test
    public void testExecuteUpdate() {
        String jpql = "UPDATE Customer c SET c.lastName = ? WHERE c.id = ?";
        Query query = entityManager.createQuery(jpql).setParameter(1, "UUU").setParameter(2, 1);
        query.executeUpdate();
    }

    //可以使用 JPQL 完成 UPDATE 和 DELETE 操作.//不设置条件就全部更新了
    @Test
    public void testExecuteDelete() {
        String jpql = "DELETE FROM Customer c WHERE c.id = ?";
        Query query = entityManager.createQuery(jpql).setParameter(1, 19);
        query.executeUpdate();
    }

    //可以使用 JPQL 完成 UPDATE 和 DELETE 操作.//不设置条件就全部更新了
//	@Test
//	public void testExecuteInsert(){
//		String jpql = "insert into Customer c values(?,?,?,?,?)";
//		Query query = entityManager.createQuery(jpql).setParameter(1,"iio").setParameter(1, 50);
//		query.executeUpdate();
//	}

    //使用 jpql 内建的函数
    @Test
    public void testJpqlFunction() {
        String jpql = "SELECT upper(c.email) FROM Customer c";
        List<String> emails = entityManager.createQuery(jpql).getResultList();
        System.out.println(emails);
    }

    @Test
    public void testSubQuery() {
        //查询所有 Customer 的 lastName 为 YY 的 Order
        String jpql = "SELECT o FROM Order o "
                + "WHERE o.customer = (SELECT c FROM Customer c WHERE c.lastName = ?)";

        Query query = entityManager.createQuery(jpql).setParameter(1, "liu1");
        List<Order> orders = query.getResultList();
        System.out.println(orders.size());
    }

    /**
     * JPQL 的关联查询同 HQL 的关联查询.
     */
    @Test
    public void testLeftOuterJoinFetch() {
        String jpql = "FROM Customer c LEFT OUTER JOIN FETCH c.orders WHERE c.id = ?";

        Customer customer =
                (Customer) entityManager.createQuery(jpql).setParameter(1, 16).getSingleResult();
        System.out.println(customer.getLastName());
//		System.out.println(customer.getOrders().size());

//		List<Object[]> result = entityManager.createQuery(jpql).setParameter(1, 12).getResultList();
//		System.out.println(result);
    }

    //查询 order 数量大于 2 的那些 Customer
    @Test
    public void testGroupBy() {
        String jpql = "SELECT o.customer FROM Order o " +
                "GROUP BY o.customer "
                + "HAVING count(o.id) >= 2";
        List<Customer> customers = entityManager.createQuery(jpql).getResultList();

        System.out.println(customers);
    }

    @Test
    public void testOrderBy() {
        String jpql = "FROM Customer c WHERE c.age > ? ORDER BY c.age DESC";
        Query query = entityManager.createQuery(jpql).setHint(QueryHints.HINT_CACHEABLE, true);

        //占位符的索引是从 1 开始
        query.setParameter(1, 15);
        List<Customer> customers = query.getResultList();
        System.out.println(customers.size());
        System.out.println(customers);
    }

    //使用 hibernate 的查询缓存.
    @Test
    public void testQueryCache() {
        String jpql = "FROM Customer c WHERE c.age > ?";
        Query query = entityManager.createQuery(jpql).setHint(QueryHints.HINT_CACHEABLE, true);

        //占位符的索引是从 1 开始
        query.setParameter(1, 1);
        List<Customer> customers = query.getResultList();
        System.out.println(customers.size());

        query = entityManager.createQuery(jpql).setHint(QueryHints.HINT_CACHEABLE, true);

        //占位符的索引是从 1 开始
        query.setParameter(1, 1);
        customers = query.getResultList();
        System.out.println(customers.size());
    }

    //createNativeQuery 适用于本地 SQL
    @Test
    public void testNativeQuery() {
        String sql = "SELECT age FROM jpa_cutomers WHERE id = ?";
        Query query = entityManager.createNativeQuery(sql).setParameter(1, 17);

        Object result = query.getSingleResult();
        System.out.println(result);
    }

    //createNativeQuery 适用于本地 SQL
    @Test
    public void testNativeQuery2() {
        String sql = "SELECT age FROM jpa_cutomers WHERE id = ?";
        Query query = entityManager.createNativeQuery(sql, "hello").setParameter(1, 17);//并指定结果集map名称

        Object result = query.getSingleResult();
        System.out.println(result);
    }

    //createNamedQuery 适用于在实体类前使用 @NamedQuery 标记的查询语句
    @Test
    public void testNamedQuery() {
        Query query = entityManager.createNamedQuery("testNamedQuery").setParameter(1, 18);
        Customer customer = (Customer) query.getSingleResult();
        System.out.println(customer);
        System.out.println(customer.getOrders());

        List list = query.getResultList();
        System.out.println(list);
    }

    //默认情况下, 若只查询部分属性, 则将返回 Object[] 类型的结果. 或者 Object[] 类型的 List.
    //也可以在实体类中创建对应的构造器, 然后再 JPQL 语句中利用对应的构造器返回实体类的对象.
    @Test
    public void testPartlyProperties() {
        String jpql = "SELECT new Customer(c.lastName, c.age) FROM Customer c WHERE c.id > ?";
        List result = entityManager.createQuery(jpql).setParameter(1, 1).getResultList();

        System.out.println(result);
    }

    @Test
    public void testHelloJPQL() {
        String jpql = "FROM Customer c WHERE c.age > ?";
        Query query = entityManager.createQuery(jpql);
        //占位符的索引是从 1 开始
        query.setParameter(1, 1);
        List<Customer> customers = query.getResultList();
        System.out.println(customers.size());
    }

    @Test
    public void testSecondLevelCache() {
        Customer customer1 = entityManager.find(Customer.class, 1);
        transaction.commit();
        entityManager.close();

        entityManager = entityManagerFactory.createEntityManager();
        transaction = entityManager.getTransaction();
        transaction.begin();

        Customer customer2 = entityManager.find(Customer.class, 1);
    }

    //对于关联的集合对象, 默认使用懒加载的策略.
    //使用维护关联关系的一方获取, 还是使用不维护关联关系的一方获取, SQL 语句相同.
    @Test
    public void testManyToManyFind() {
//		Item item = entityManager.find(Item.class, 2);
//		System.out.println(item.getItemName());

//		System.out.println(item.getCategories().size());

        Category category = entityManager.find(Category.class, 2);
        System.out.println(category.getCategoryName());
        System.out.println(category.getItems().size());
    }

    //多对所的保存
    @Test
    public void testManyToManyPersist() {
        Item i1 = new Item();
        i1.setItemName("i-1");

        Item i2 = new Item();
        i2.setItemName("i-2");

        Category c1 = new Category();
        c1.setCategoryName("C-1");

        Category c2 = new Category();
        c2.setCategoryName("C-2");

        //设置关联关系
        i1.getCategories().add(c1);
        i1.getCategories().add(c2);

        i2.getCategories().add(c1);
        i2.getCategories().add(c2);

        c1.getItems().add(i1);
        c1.getItems().add(i2);

        c2.getItems().add(i1);
        c2.getItems().add(i2);

        //执行保存
        entityManager.persist(i1);
        entityManager.persist(i2);
        entityManager.persist(c1);
        entityManager.persist(c2);
    }


    @Test
    public void shuaxin() {
        entityManager.flush();
    }

    //1. 默认情况下, 若获取不维护关联关系的一方, 则也会通过左外连接获取其关联的对象.
    //可以通过 @OneToOne 的 fetch 属性来修改加载策略. 但依然会再发送 SQL 语句来初始化其关联的对象
    //这说明在不维护关联关系的一方, 不建议修改 fetch 属性.
    @Test
    public void testOneToOneFind2() {
        Manager mgr = entityManager.find(Manager.class, 2);
//        System.out.println(mgr.getMgrName());

        System.out.println(mgr.getDept().getDeptName());
    }

    //1.默认情况下, 若获取维护关联关系的一方, 则会通过左外连接获取其关联的对象.
    //但可以通过 @OntToOne 的 fetch 属性来修改加载策略.
    @Test
    public void testOneToOneFind() {
        Department dept = entityManager.find(Department.class, 2);
//		System.out.println(dept.getDeptName());
        System.out.println(dept.getMgr().getMgrName());
    }

    //双向 1-1 的关联关系, 建议先保存不维护关联关系的一方, 即没有外键的一方, 这样不会多出 UPDATE 语句.
    @Test
    public void testOneToOnePersistence() {
        Manager mgr = new Manager();
        mgr.setMgrName("M-BB");

        Department dept = new Department();
        dept.setDeptName("D-BB");

        //设置关联关系
        mgr.setDept(dept);
        dept.setMgr(mgr);

        //执行保存操作
        entityManager.persist(mgr);
        entityManager.persist(dept);
    }

    //双向 1-1 的关联关系, 先保存外键的一方, 会多出 UPDATE 语句.
    @Test
    public void testOneToOnePersistence2() {
        Manager mgr = new Manager();
        mgr.setMgrName("M-BB");

        Department dept = new Department();
        dept.setDeptName("D-BB");

        //设置关联关系
        mgr.setDept(dept);
        dept.setMgr(mgr);

        //执行保存操作
        entityManager.persist(dept);
        entityManager.persist(mgr);
    }


    //若是双向 1-n 的关联关系, 执行保存时
    //若先保存 n 的一端, 再保存 1 的一端, 默认情况下, 会多出 n 条 UPDATE 语句.
    //若先保存 1 的一端, 则会多出 n 条 UPDATE 语句
    //在进行双向 1-n 关联关系时, 建议使用 n 的一方来维护关联关系, 而 1 的一方不维护关联系, 这样会有效的减少 SQL 语句.
    //注意: 若在 1 的一端的 @OneToMany 中使用 mappedBy 属性, 则 @OneToMany 端就不能再使用 @JoinColumn 属性了.

    //单向 1-n 关联关系执行保存时, 一定会多出 UPDATE 语句.
    //因为 n 的一端在插入时不会同时插入外键列.

    //查1的一方也会把n查出来 根据加载策略sql条数不同
	/*@Test
	public void testManyToOneUpdate2(){
		Customer customer = entityManager.find(Customer.class, 14);

		customer.getOrders().iterator().next().setOrderName("O-XXX-10");
//		System.out.println(customer.getOrders());
	}

	//查n的一方默认会将1一方也查出来，根据加载策略不同sql条数不同2/3
	@Test
	public void testManyToOneUpdate(){
		Order order = entityManager.find(Order.class, 2);
		order.getCustomer().setLastName("FFF");
	}

	//默认情况下, 若删除 1 的一端, 则会先把关联的 n 的一端的外键置空, 然后进行删除.
	//可以通过 @OneToMany 的 cascade 属性来修改默认的删除策略.
	@Test
	public void testOneToManyRemove(){
		Customer customer = entityManager.find(Customer.class, 8);
		entityManager.remove(customer);
	}

	//不能直接删除 1 的一端, 因为有外键约束. 配置级联删除/先删多的一方
	@Test
	public void testManyToOneRemove(){
//		Order order = entityManager.find(Order.class, 1);
//		entityManager.remove(order);

		//未开启级联删除不可删，开启级联删除可删
		Customer customer = entityManager.find(Customer.class, 7);
		entityManager.remove(customer);
	}

	//默认情况下, 使用左外连接的方式来获取 n 的一端的对象和其关联的 1 的一端的对象.
	//可使用 @ManyToOne 的 fetch 属性来修改默认的关联属性的加载策略
	@Test
	public void testManyToOneFind(){
		Order order = entityManager.find(Order.class, 2);
		System.out.println(order.getOrderName());

		System.out.println(order.getCustomer().getLastName());
	}

	//默认对关联的多的一方使用懒加载的加载策略.
	//可以使用 @OneToMany 的 fetch 属性来修改默认的加载策略
	@Test
	public void testOneToManyFind(){
		Customer customer = entityManager.find(Customer.class, 14);
		System.out.println(customer.getLastName());

		System.out.println(customer.getOrders().size());
	}

	 //保存多对一时, 建议先保存 1 的一端, 后保存 n 的一端, 这样不会多出额外的 UPDATE 语句. 采用表策略主键共5条
	@Test
	public void testManyToOnePersist(){
		Customer customer = new Customer();
		customer.setAge(18);
		customer.setBirth(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("mo@163.com");
		customer.setLastName("mo");

		Order order1 = new Order();
		order1.setOrderName("11-GG-1");

		Order order2 = new Order();
		order2.setOrderName("22-GG-2");

		//设置关联关系
		order1.setCustomer(customer);
		order2.setCustomer(customer);

		entityManager.persist(customer);
		//执行保存操作
		entityManager.persist(order1);
		entityManager.persist(order2);

	}

	//多对一，先保存多的一方 7条 最后2条更新order
	@Test
	public void testManyToOnePersist2(){
		Customer customer = new Customer();
		customer.setAge(23);
		customer.setBirth(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("1123@163.com");
		customer.setLastName("LIU");

		Order order1 = new Order();
		order1.setOrderName("66-LL-1");

		Order order2 = new Order();
		order2.setOrderName("77-LL-2");

		//设置关联关系
		order1.setCustomer(customer);
		order2.setCustomer(customer);
		//执行保存操作
		entityManager.persist(order1);
		entityManager.persist(order2);
		entityManager.persist(customer);
	}*/

    /*判断实例是否被上下文环境管理*/

    /**
     *
     */
    @Test
    public void containsAndClear() {
        Customer customer = entityManager.find(Customer.class, 7);
        boolean contains = entityManager.contains(customer);
        entityManager.clear();//清除上下文环境
        boolean contains2 = entityManager.contains(customer);
        System.out.println(contains);//true
        System.out.println(contains2);//flase
        System.out.println(entityManager.isOpen());
        System.out.println(entityManager.getTransaction());
    }

    /**
     *
     *
     * 同 hibernate 中 Session 的 refresh 方法.
     */
    @Test
    public void testRefresh() throws InterruptedException {
        Customer customer = this.entityManager.find(Customer.class, 1);
        customer = this.entityManager.find(Customer.class, 1);
        System.out.println("开始");
        Thread.sleep(10000);
        this.entityManager.refresh(customer);
        System.out.println(customer);


//        EntityManager em2 = entityManagerFactory.createEntityManager();
//        EntityTransaction transaction = em2.getTransaction();
//        transaction.begin();
//         Customer customer1 = em2.find(Customer.class, 1);
//          customer1 = em2.find(Customer.class, 1);
//        System.out.println(em2.contains(customer1));
//        customer1.setId(2);
//        System.out.println(em2.contains(customer1));
//        em2.refresh(customer1);
//        transaction.commit();
    }

    /**
     * 同 hibernate 中 Session 的 flush 方法.
     */
    @Test
    public void testFlush() {
        Customer customer = entityManager.find(Customer.class, 1);
        System.out.println(customer);

        customer.setLastName("AA");

        entityManager.flush();
        FlushModeType flushMode = entityManager.getFlushMode();
    }

    //若传入的是一个游离对象, 即传入的对象有 OID.  第一查放入缓存，第二次直接update 2条sql
    //1. 若在 EntityManager 缓存中有对应的对象
    //2. JPA 会把游离对象的属性复制到查询到EntityManager 缓存中的对象中.
    //3. EntityManager 缓存中的对象执行 UPDATE.
    @Test
    public void testMerge4() {
        Customer customer = new Customer();
        customer.setAge(18);
        customer.setBirth(new Date());
        customer.setCreatedTime(new Date());
        customer.setEmail("dd@163.com");
        customer.setLastName("DD");

        customer.setId(2);//会先调用一条
        Customer customer2 = entityManager.find(Customer.class, 1);

        entityManager.merge(customer);

        System.out.println(customer == customer2); //false
    }

    //若传入的是一个游离对象, 即传入的对象有 OID.
    //1. 若在 EntityManager 缓存中没有该对象
    //2. 若在数据库中也有对应的记录
    //3. JPA 会查询对应的记录, 然后返回该记录对一个的对象, 再然后会把游离对象的属性复制到查询到的对象中.
    //4. 对查询到的对象执行 update 操作.
    @Test
    public void testMerge3() {
        Customer customer = new Customer();
        customer.setAge(18);
        customer.setBirth(new Date());
        customer.setCreatedTime(new Date());
        customer.setEmail("ee@163.com");
        customer.setLastName("EE");

        customer.setId(10);

        Customer customer2 = entityManager.merge(customer);

        System.out.println(customer == customer2); //false
    }

    //若传入的是一个游离对象, 即传入的对象有 OID.
    //1. 若在 EntityManager 缓存中没有该对象
    //2. 若在数据库中也没有对应的记录
    //3. JPA 会创建一个新的对象, 然后把当前游离对象的属性复制到新创建的对象中
    //4. 对新创建的对象执行 insert 操作.
    @Test
    public void testMerge2() {
        Customer customer = new Customer();
        customer.setAge(18);
        customer.setBirth(new Date());
        customer.setCreatedTime(new Date());
        customer.setEmail("dd@163.com");
        customer.setLastName("DD");

        customer.setId(7);

        Customer customer2 = entityManager.merge(customer);

        System.out.println("customer#id:" + customer.getId());
        System.out.println("customer2#id:" + customer2.getId());
    }

    /**
     * 总的来说: 类似于 hibernate Session 的 saveOrUpdate 方法.
     */
    //1. 若传入的是一个临时对象
    //会创建一个新的对象, 把临时对象的属性复制到新的对象中, 然后对新的对象执行持久化操作. 所以
    //新的对象中有 id, 但以前的临时对象中没有 id.
    @Test
    public void testMerge1() {
        Customer customer = new Customer();
        customer.setAge(18);
        customer.setBirth(new Date());
        customer.setCreatedTime(new Date());
        customer.setEmail("cc@163.com");
        customer.setLastName("CC");

        Customer customer2 = entityManager.merge(customer);

        System.out.println("customer#id:" + customer.getId());
        System.out.println("customer2#id:" + customer2.getId());
    }

    //类似于 hibernate 中 Session 的 delete 方法. 把对象对应的记录从数据库中移除
    //但注意: 该方法只能移除 持久化 对象. 而 hibernate 的 delete 方法实际上还可以移除 游离对象.
    //cascade={CascadeType.REMOVE}，否则无法删除costomer，因为被order当外键引用
    @Test
    public void testRemove() {
//		Customer customer = new Customer();
//		customer.setId(2);

        Customer customer = entityManager.find(Customer.class, 1);
        entityManager.remove(customer);
    }

    //更新，因为方法执行会才提交事务
    @Test
    public void testUpdate1() {
        Customer customer = entityManager.find(Customer.class, 1);
//		transaction.commit();
//		transaction.begin();
        customer.setLastName("lp123");
//		entityManager.persist(customer);//不需要这步也可以
    }

    //类似于 hibernate 的 save 方法. 使对象由临时状态变为持久化状态.
    //和 hibernate 的 save 方法的不同之处: 若对象有 id, 则不能执行 insert 操作, 而会抛出异常.
    @Test
    public void testPersistence() {
        Customer customer = new Customer();
        customer.setAge(789);
        customer.setBirth(new Date());
        customer.setCreatedTime(new Date());
        customer.setEmail("bb1@163.com");
        customer.setLastName("ecen");
        Order order1 = new Order();
        order1.setOrderName("11-GG-1");
        order1.setCustomer(customer);
        Set set = new HashSet();
        set.add(order1);
        customer.setOrders(set);
//		customer.setId(100);
        entityManager.persist(customer);//保存也会缓存 如果后面不需要用到该对象 应该及时clear释放内存  persit返回的还是原来的对象
        System.out.println(customer.getOrders());
//        System.out.println(customer.getId());
//        entityManager.clear();
//        Customer customer1 = entityManager.find(Customer.class, 24);
//        System.out.println(customer1);
//        System.out.println(customer1 ==customer);
    }

    @Test
    public void testGetSingleCache(){
        String jpql = "FROM Customer c WHERE c.id = ?";

        Customer customer =
                (Customer) entityManager.createQuery(jpql).setParameter(1, 24).getSingleResult();
        System.out.println(customer.getLastName());
        Customer customer1 = entityManager.find(Customer.class, 24);
        System.out.println(customer1);
        customer1.setLastName("55556");
        entityManager.flush();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(customer1==customer);

        //在事务提交flush之前clear掉了 不会更改到，如果没有clear 则会被更改
//        customer1.setLastName("abc");
//        entityManager.clear();
    }

    //类似于 hibernate 中 Session 的 load 方法 第一次查找，若实体不存在会抛异常
    @Test
    public void testGetReference() {
        Customer customer = entityManager.getReference(Customer.class, 1);
        System.out.println(customer.getClass().getName());

        System.out.println("-------------------------------------");
//		transaction.commit();
//		entityManager.close();

        System.out.println(customer);
    }

    //类似于 hibernate 中 Session 的 get 方法. 未找到返回null，关系eager左外连接，lazy用到查两次
    @Test
    public void testFind() {
        Customer customer = entityManager.find(Customer.class, 28);
        System.out.println("-------------------------------------");
        Customer customer1 = customer;
        customer.setLastName("111111131313");
        System.out.println(customer);
//        Set<Order> orders = customer.getOrders();
//        System.out.println(orders);
    }

    @Test
    public void testFind2() {
        Customer customer = new Customer();
        customer.setId(2);
        customer = entityManager.find(Customer.class, 1);
        System.out.println("-------------------------------------");
    }

}
