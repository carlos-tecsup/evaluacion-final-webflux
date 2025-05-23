package com.mitocode.controller;

import com.mitocode.dto.StudentDTO;
import com.mitocode.model.Student;
import com.mitocode.service.IStudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final IStudentService studentService;

    @Qualifier("defaultMapper")
    private final ModelMapper modelMapper;

    @PostMapping("/create")
    public Mono<ResponseEntity<StudentDTO>> save(@Valid @RequestBody StudentDTO studentDTO, ServerHttpRequest req){
        return studentService.save(modelMapper.map(studentDTO, Student.class))
                .map(this::convertToDto)
                .map(e -> ResponseEntity.created(
                                        URI.create(req.getURI().toString().concat("/").concat(String.valueOf(e.getId())))
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(e)
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @GetMapping
    public Mono<ResponseEntity<Flux<StudentDTO>>> findAllSortByAge(@RequestParam(required = false) String direction){
        Flux<StudentDTO> fx = studentService.findAllSortedByAge(direction).map(this::convertToDto);

        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fx)
        ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public Mono<ResponseEntity<StudentDTO>> update(@Valid @PathVariable("id") String id, @RequestBody StudentDTO dto){
        return Mono.just(dto)
                .map(e->{
                    e.setId(id);
                return e;
                })
                .flatMap(e-> studentService.update(id, convertToDocument(e)))
                .map(this::convertToDto)
                .map(e->ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id){
        return studentService.delete(id)
                .flatMap(result->{
                    if(result){
                        return Mono.just(ResponseEntity.noContent().build());
                    } else {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                });
    }


    private StudentDTO convertToDto(Student model){
        return modelMapper.map(model, StudentDTO.class);
    }

    private Student convertToDocument(StudentDTO dto){
        return modelMapper.map(dto, Student.class);
    }
}
