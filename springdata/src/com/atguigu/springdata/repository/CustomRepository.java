/*
 * Copyright 2019 Guangdong Etone Technology Co.,Ltd.
 * All rights reserved.
 */
package com.atguigu.springdata.repository;


import com.atguigu.springdata.Person;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * 一句简洁的说明
 *
 * @author <a href="mailto:maxid@qq.com">LiuTao</a>
 * @since $$Id$$
 */
@RepositoryDefinition(domainClass=Person.class,idClass=Integer.class)
public interface CustomRepository/* extends Repository<Person,Integer>*/ {
    Person getPersonById(Integer id);
    Person findOne(Integer id);
}
