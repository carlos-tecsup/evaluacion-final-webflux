package com.mitocode.repository;

import com.mitocode.model.User;
import reactor.core.publisher.Mono;


public interface IUserRepository extends IGenericRepository<User, String> {
    //@Query("{ username:  ?1}")
    Mono<User> findOneByUsername(String username);

}
