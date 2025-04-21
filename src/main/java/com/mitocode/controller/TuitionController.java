package com.mitocode.controller;

import com.mitocode.dto.TuitionDTO;
import com.mitocode.dto.TuitionResponse;
import com.mitocode.model.Tuition;
import com.mitocode.service.ITuitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/tuition")
@RequiredArgsConstructor
public class TuitionController {
    private final ITuitionService iTuitionService;
    @Qualifier("tuitionMapper")
    private final ModelMapper modelMapper;

    @PostMapping("/create")
    public Mono<ResponseEntity<TuitionResponse>> save(@Valid @RequestBody TuitionDTO tuitionDTO, ServerHttpRequest req){
        return  iTuitionService.registerTuition(modelMapper.map(tuitionDTO, TuitionDTO.class))
                .map(this::convertToDto)
                .map(e -> ResponseEntity.created(
                                        URI.create(req.getURI().toString().concat("/").concat(String.valueOf(e.getId())))
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(e)
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }

    private TuitionResponse convertToDto(Tuition model){
        return modelMapper.map(model, TuitionResponse.class);
    }

}
