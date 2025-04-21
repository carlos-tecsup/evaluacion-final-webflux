package com.mitocode.repository;

import com.mitocode.model.Tuition;
import org.springframework.data.mongodb.repository.Query;
import reactor.core.publisher.Mono;

public interface ITuitionRepository extends IGenericRepository<Tuition, String>{
    @Query(value = "{ 'student.dni': ?0 }", exists = true)
    Mono<Boolean> existsTuitionByStudentDni(String studentDni);
}
