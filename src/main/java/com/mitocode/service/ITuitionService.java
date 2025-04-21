package com.mitocode.service;

import com.mitocode.dto.TuitionDTO;
import com.mitocode.model.Tuition;
import reactor.core.publisher.Mono;

public interface ITuitionService extends ICRUD<Tuition, String> {
    Mono<Tuition> registerTuition(TuitionDTO tuitionDTO);

}
