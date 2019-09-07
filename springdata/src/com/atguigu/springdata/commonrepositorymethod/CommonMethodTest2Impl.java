package com.atguigu.springdata.commonrepositorymethod;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * 为所有的 Repository 都添加自实现的方法
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public class CommonMethodTest2Impl<T, ID extends Serializable>
	extends SimpleJpaRepository<T, ID> implements CommonMethodTest<T, ID> {

	public CommonMethodTest2Impl(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
	}

	@Override
	public void method() {
		System.out.println("...METHOD TEST2...");
	}

}
