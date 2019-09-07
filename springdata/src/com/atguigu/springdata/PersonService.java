package com.atguigu.springdata;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonService {

	@Autowired
	private PersonRepsotory personRepsotory;

	@Transactional
	public void savePersons(List<Person> persons){
		personRepsotory.save(persons);
	}

	@Transactional
	public void updatePersonEmail(String email, Integer id){
		personRepsotory.updatePersonEmail(id, email);
	}
}
