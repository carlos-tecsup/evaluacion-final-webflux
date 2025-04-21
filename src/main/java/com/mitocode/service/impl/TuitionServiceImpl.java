package com.mitocode.service.impl;

import com.mitocode.dto.TuitionDTO;
import com.mitocode.model.Course;
import com.mitocode.model.Student;
import com.mitocode.model.Tuition;
import com.mitocode.repository.ICourseRepository;
import com.mitocode.repository.IGenericRepository;
import com.mitocode.repository.IStudentRepository;
import com.mitocode.repository.ITuitionRepository;
import com.mitocode.service.ITuitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TuitionServiceImpl extends CRUDImpl<Tuition, String> implements ITuitionService {

    private final ITuitionRepository tuitionRepository;
    private final IStudentRepository studentRepository;
    private final ICourseRepository courseRepository;

    @Autowired
    private SequenceGeneratorServiceImpl sequenceGeneratorService;

    @Override
    protected IGenericRepository<Tuition, String> getRepo() {
        return tuitionRepository;
    }

    @Override
    public Mono<Tuition> registerTuition(TuitionDTO tuitionDTO) {
        Mono<Student> studentFound = studentRepository.findStudentByDni(tuitionDTO.getDniStudent())
                .switchIfEmpty(Mono.error(new RuntimeException("Estudiante no encontrado")));


        Mono<List<Course>> coursesFound = Flux.fromIterable(tuitionDTO.getCoursesName())
                .flatMap(name ->
                        courseRepository.findCourseByNameAndStatusIsTrue(name)
                                .switchIfEmpty(Mono.error(new RuntimeException("Curso no encontrado: " + name)))
                )
                .collectList();

        return Mono.zip(studentFound, coursesFound)
                .flatMap(tuple -> {
                    Student studentTuition = tuple.getT1();
                    List<Course> coursesTuition = tuple.getT2();
                    return sequenceGeneratorService.generateSequence("tuition_sequence")
                            .flatMap(tuitionId -> {
                                // Crear la entidad de Tuition
                                Tuition tuition = Tuition.builder()
                                        .id(tuitionId)  // Asignar el tuitionId generado
                                        .student(studentTuition)
                                        .courses(coursesTuition)
                                        .registrationDate(LocalDateTime.now())
                                        .status(true)
                                        .build();

                                // Guardar la matrícula en la base de datos
                                return tuitionRepository.save(tuition);
                            });
                });
    }
}
