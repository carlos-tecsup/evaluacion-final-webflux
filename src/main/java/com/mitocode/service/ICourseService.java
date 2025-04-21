package com.mitocode.service;

import com.mitocode.model.Course;
import reactor.core.publisher.Mono;

public interface ICourseService extends ICRUD<Course, String> {
    Mono<Boolean> deleteCourse(String id);
}
