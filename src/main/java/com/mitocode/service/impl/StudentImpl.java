package com.mitocode.service.impl;

import com.mitocode.model.Student;
import com.mitocode.repository.IGenericRepository;
import com.mitocode.repository.IStudentRepository;
import com.mitocode.service.IStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public  class StudentImpl extends CRUDImpl<Student, Long> implements IStudentService {

    private final IStudentRepository studentRepository;

    @Autowired
    private SequenceGeneratorServiceImpl sequenceGeneratorService;

    @Override
    protected IGenericRepository<Student, Long> getRepo() {
        return studentRepository;
    }


    @Override
    public Mono<Student> save(Student student) {
        if (student.getId() == null) {
            return sequenceGeneratorService.generateSequence("producto_sequence")
                    .map(id -> {
                        student.setId(id);
                        return student;
                    })
                    .flatMap(studentRepository::save);
        } else {
            return studentRepository.save(student);
        }
    }

}
