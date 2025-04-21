package com.mitocode.service.impl;

import com.mitocode.model.Course;
import com.mitocode.repository.ICourseRepository;
import com.mitocode.repository.IGenericRepository;
import com.mitocode.service.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl extends CRUDImpl<Course, String> implements ICourseService {
    private final ICourseRepository courseRepository;
    @Autowired
    private SequenceGeneratorServiceImpl sequenceGeneratorService;

    @Override
    protected IGenericRepository<Course, String> getRepo() {
        return courseRepository;
    }

    @Override
    public Mono<Course> save(Course course) {
        return sequenceGeneratorService.generateSequence("course_sequence")
                .doOnNext(course::setId)
                .doOnNext(id -> course.setStatus(true))
                .then(courseRepository.save(course));
    }

}
