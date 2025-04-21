package com.mitocode.controller;

import com.mitocode.dto.CourseDTO;
import com.mitocode.dto.StudentDTO;
import com.mitocode.model.Course;
import com.mitocode.model.Student;
import com.mitocode.service.ICourseService;
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
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {
    private final ICourseService courseService;

    @Qualifier("defaultMapper")
    private final ModelMapper modelMapper;

    @PostMapping("/create")
    public Mono<ResponseEntity<CourseDTO>> save(@Valid @RequestBody CourseDTO courseDTO, ServerHttpRequest req){
        return courseService.save(modelMapper.map(courseDTO, Course.class))
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
    public Mono<ResponseEntity<Flux<CourseDTO>>> findAll(){
        Flux<CourseDTO> fx = courseService.findAll().map(this::convertToDto);

        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fx)
        ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public Mono<ResponseEntity<CourseDTO>> update(@Valid @PathVariable("id") String id, @RequestBody CourseDTO dto){
        return Mono.just(dto)
                .map(e->{
                    e.setId(id);
                    return e;
                })
                .flatMap(e-> courseService.update(id, convertToDocument(e)))
                .map(this::convertToDto)
                .map(e->ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id){
        return courseService.delete(id)
                .flatMap(result->{
                    if(result){
                        return Mono.just(ResponseEntity.noContent().build());
                    } else {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                });
    }

    private CourseDTO convertToDto(Course model){
        return modelMapper.map(model, CourseDTO.class);
    }

    private Course convertToDocument(CourseDTO dto){
        return modelMapper.map(dto, Course.class);
    }

}
