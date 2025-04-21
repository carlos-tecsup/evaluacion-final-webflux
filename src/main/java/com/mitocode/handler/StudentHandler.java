package com.mitocode.handler;

import com.mitocode.dto.StudentDTO;
import com.mitocode.model.Student;
import com.mitocode.service.IStudentService;
import com.mitocode.validator.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class StudentHandler {

    private final IStudentService studentService;

    @Qualifier("defaultMapper")
    private final ModelMapper modelMapper;

    private final RequestValidator requestValidator;

    public Mono<ServerResponse> save(ServerRequest request) {
        Mono<StudentDTO> monoStudentDTO = request.bodyToMono(StudentDTO.class);

        return monoStudentDTO
                .flatMap(requestValidator::validate)
                .flatMap(e -> studentService.save(convertToDocument(e)))
                .map(this::convertToDto)
                .flatMap(e -> ServerResponse
                        .created(URI.create(request.uri().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(e))
                );
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        String id = request.pathVariable("id");

        Mono<StudentDTO> monoStudentDTO = request.bodyToMono(StudentDTO.class);

        return monoStudentDTO
                .flatMap(requestValidator::validate)
                .map(e -> {
                    e.setId(id);
                    return e;
                })
                .flatMap(e -> studentService.update(id, convertToDocument(e)))
                .map(this::convertToDto)
                .flatMap(e -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(e))
                );
    }

    public Mono<ServerResponse> findAllSortedByAge(ServerRequest request) {
        String direction = request.queryParam("direction").orElse(null);

        Flux<StudentDTO> flux = studentService.findAllSortedByAge(direction)
                .map(this::convertToDto);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(flux, StudentDTO.class);
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");

        return studentService.delete(id)
                .flatMap(result -> {
                    if(result){
                        return ServerResponse.noContent().build();
                    }else{
                        return ServerResponse.notFound().build();
                    }
                });
    }

    private StudentDTO convertToDto(Student model) {
        return modelMapper.map(model, StudentDTO.class);
    }

    private Student convertToDocument(StudentDTO dto) {
        return modelMapper.map(dto, Student.class);
    }

}
