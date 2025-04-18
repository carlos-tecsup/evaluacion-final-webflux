package com.mitocode.repository;

import com.mitocode.model.Role;
import reactor.core.publisher.Mono;

public interface IRoleRepository extends IGenericRepository<Role, String> {
    Mono<Role> findByName(String name);

}
