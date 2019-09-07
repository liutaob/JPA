package com.atguigu.springdata.repository;

import com.atguigu.springdata.Person;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CustomRepositoryTest {

    private ApplicationContext ctx = null;
    private CustomRepository repository;
    private CustomBaseRepository baseRepository;
    private CustomImplRepository customImplRepository;

    /*@Before
    public void init()*/{
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        repository = ctx.getBean(CustomRepository.class);
        baseRepository = ctx.getBean(CustomBaseRepository.class);
        customImplRepository = ctx.getBean(CustomImplRepository.class);
    }

    @Test
    public void getPersonById() {
        Person person = repository.getPersonById(1);
        System.out.println(person.getAddress());
//        Address address = repository.getAddressById(1);
    }

    @Test
    public void findOne(){
        Person person = repository.findOne(25);
        Assert.assertNotNull(person);
    }
    
    @Test
    public void find(){
        System.out.println(baseRepository.getClass().getName());
        Person person = customImplRepository.findPersonById(1);
        Assert.assertNotNull(person);
    }

}
