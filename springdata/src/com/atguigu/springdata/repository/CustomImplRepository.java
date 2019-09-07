/*
 * Copyright 2019 Guangdong Etone Technology Co.,Ltd.
 * All rights reserved.
 */
package com.atguigu.springdata.repository;

import com.atguigu.springdata.Person;

/**
 * 一句简洁的说明
 *
 * @author <a href="mailto:maxid@qq.com">LiuTao</a>
 * @since $$Id$$
 */
public interface CustomImplRepository extends CustomBaseRepository<Person,Integer> {
    Person findPersonById(Integer id);
}
