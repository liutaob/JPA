package com.atguigu.jpa.service;

import com.atguigu.jpa.dao.PersonDao;
import com.atguigu.jpa.spring.entities.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonService {

    @Autowired
    private PersonDao personDao;


    @Transactional
    public void savePersons(Person p1, Person p2) {
        personDao.save(p1);

//		int i = 10 / 0;

        personDao.save(p2);
    }


}
