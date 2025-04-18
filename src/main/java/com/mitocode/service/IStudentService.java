package com.mitocode.service;

import com.mitocode.model.Student;
import com.mitocode.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IStudentService extends ICRUD<Student, Long> {
}
