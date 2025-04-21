package com.mitocode.service.impl;

import com.mitocode.model.Student;
import com.mitocode.repository.IGenericRepository;
import com.mitocode.repository.IStudentRepository;
import com.mitocode.service.IStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public  class StudentServiceImpl extends CRUDImpl<Student, String> implements IStudentService {

    private final IStudentRepository studentRepository;

    @Autowired
    private SequenceGeneratorServiceImpl sequenceGeneratorService;

    @Override
    protected IGenericRepository<Student, String> getRepo() {
        return studentRepository;
    }


    @Override
    public Mono<Student> save(Student student) {
        return sequenceGeneratorService.generateSequence("student_sequence")
                .doOnNext(student::setId)
                .then(studentRepository.save(student));
    }

    @Override
    public Flux<Student> findAllSortedByAge(String direction) {
        if (direction != null && !direction.isBlank()) {
            Sort.Direction dir = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
            return studentRepository.findAll(Sort.by(dir, "age"));
        }
        return studentRepository.findAll();
    }
}
