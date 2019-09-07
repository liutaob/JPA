package com.atguigu.springdata.commonrepositorymethod;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CommonMethodTest<T, ID extends Serializable> 
	extends JpaRepository<T, ID>{

	void method();
	
}
