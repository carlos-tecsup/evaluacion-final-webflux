package com.mitocode.service;

import com.mitocode.model.Student;
import reactor.core.publisher.Flux;

public interface IStudentService extends ICRUD<Student, String> {
    Flux<Student> findAllSortedByAge(String direction);
}
