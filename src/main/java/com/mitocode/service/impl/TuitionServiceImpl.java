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
        // Paso 1: Validar si el estudiante ya está matriculado
        return tuitionRepository.existsTuitionByStudentDni(tuitionDTO.getDniStudent())
                .flatMap(isEnrolled -> {
                    if (isEnrolled) {
                        return Mono.error(new RuntimeException("El estudiante ya está matriculado."));
                    }
                    return Mono.empty();
                })
                // Paso 2: Validar si el estudiante existe y los cursos son válidos
                .then(
                        studentRepository.findStudentByDni(tuitionDTO.getDniStudent())
                                .switchIfEmpty(Mono.error(new RuntimeException("Estudiante no encontrado")))
                                .zipWith(
                                        Flux.fromIterable(tuitionDTO.getCoursesName())
                                                .flatMap(name ->
                                                        courseRepository.findCourseByNameAndStatusIsTrue(name)
                                                                .switchIfEmpty(Mono.error(new RuntimeException("Curso no encontrado: " + name)))
                                                )
                                                .collectList()
                                )
                )
                // Paso 3: Crear la matrícula
                .flatMap(tuple -> {
                    Student student = tuple.getT1();
                    List<Course> courses = tuple.getT2();

                    return sequenceGeneratorService.generateSequence("tuition_sequence")
                            .flatMap(tuitionId -> {
                                Tuition tuition = Tuition.builder()
                                        .id(tuitionId)
                                        .student(student)
                                        .courses(courses)
                                        .registrationDate(LocalDateTime.now())
                                        .status(true)
                                        .build();

                                return tuitionRepository.save(tuition);
                            });
                });
    }
}
