package com.mitocode.handler;

import com.mitocode.dto.TuitionDTO;
import com.mitocode.dto.TuitionResponse;
import com.mitocode.model.Tuition;
import com.mitocode.service.ITuitionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;


@Component
@RequiredArgsConstructor
public class TuitionHandler {
    private final ITuitionService tuitionService;

    @Qualifier("tuitionMapper")
    private final ModelMapper modelMapper;

    public Mono<ServerResponse> save(ServerRequest request) {
        Mono<TuitionDTO> monoTuitionDTO = request.bodyToMono(TuitionDTO.class);

        return monoTuitionDTO
                .flatMap(tuitionService::registerTuition)
                .map(this::convertToDto)
                .flatMap(response -> ServerResponse
                        .created(URI.create(request.uri().toString().concat("/").concat(response.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response)
                );
    }

    private TuitionResponse convertToDto(Tuition model) {
        return modelMapper.map(model, TuitionResponse.class);
    }

}
