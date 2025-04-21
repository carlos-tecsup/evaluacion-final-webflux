package com.mitocode.repository;

import com.mitocode.model.Student;
import reactor.core.publisher.Mono;

public interface IStudentRepository extends IGenericRepository<Student, String> {
    Mono<Student> findStudentByDni(String dni);
}
