package com.mitocode.repository;

import com.mitocode.model.Course;
import org.springframework.data.mongodb.repository.Query;
import reactor.core.publisher.Mono;


public interface ICourseRepository extends IGenericRepository<Course, String> {
    Mono<Course> findCourseByNameAndStatusIsTrue(String courseName);
}
