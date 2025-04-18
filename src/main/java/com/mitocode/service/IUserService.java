package com.mitocode.service;

import com.mitocode.model.User;
import com.mitocode.dto.AuthRegisterDTO;
import reactor.core.publisher.Mono;

public interface IUserService extends ICRUD<User, String>{

    Mono<User> registerUser(AuthRegisterDTO user);
    Mono<com.mitocode.security.User> searchByUser(String username);
}
