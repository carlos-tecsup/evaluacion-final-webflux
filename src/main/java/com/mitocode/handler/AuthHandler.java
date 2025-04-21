package com.mitocode.handler;

import com.mitocode.service.ICourseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.mitocode.dto.AuthLoginDTO;
import com.mitocode.dto.AuthRegisterDTO;
import com.mitocode.dto.LoginResponseDTO;
import com.mitocode.security.JwtUtil;
import com.mitocode.service.IUserService;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final IUserService service;
    private final JwtUtil jwtUtil;

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(AuthLoginDTO.class)
                .flatMap(auth -> service.searchByUser(auth.getUsername())
                        .flatMap(userDetails -> {
                            if (BCrypt.checkpw(auth.getPassword(), userDetails.getPassword())) {
                                String token = jwtUtil.generateToken(userDetails);
                                Date expiration = jwtUtil.getExpirationDateFromToken(token);
                                return ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(new LoginResponseDTO(token, expiration));
                            } else {
                                return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
                            }
                        })
                        .switchIfEmpty(ServerResponse.status(HttpStatus.UNAUTHORIZED).build()));
    }

    public Mono<ServerResponse> register(ServerRequest request) {
        return request.bodyToMono(AuthRegisterDTO.class)
                .flatMap(service::registerUser)
                .flatMap(user -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user));
    }
}
