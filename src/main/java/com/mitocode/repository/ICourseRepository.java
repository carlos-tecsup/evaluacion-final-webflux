package com.mitocode.repository;

import com.mitocode.model.Course;
import reactor.core.publisher.Mono;


public interface ICourseRepository extends IGenericRepository<Course, String> {
    Mono<Course> findCourseByName(String courseName);
}
