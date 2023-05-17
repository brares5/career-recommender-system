package com.crs.service;

import com.crs.model.Job;
import com.crs.model.Person;

import java.util.List;

public interface PersonService {

    void createPerson(Person p);
    List<Job> classifyPerson(Person p);

}
