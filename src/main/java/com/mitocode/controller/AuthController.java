package com.mitocode.controller;

import com.mitocode.model.User;
import com.mitocode.dto.AuthRegisterDTO;
import com.mitocode.dto.AuthLoginDTO;
import com.mitocode.dto.LoginResponseDTO;
import com.mitocode.security.JwtUtil;
import com.mitocode.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Date;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final IUserService service;

    @PostMapping("/login")
    public Mono<ResponseEntity<?>> login(@RequestBody AuthLoginDTO authLoginDTO){
        return service.searchByUser(authLoginDTO.getUsername())
                .map(userDetails -> {
                    if(BCrypt.checkpw(authLoginDTO.getPassword(), userDetails.getPassword())){
                        String token = jwtUtil.generateToken(userDetails);
                        Date expiration = jwtUtil.getExpirationDateFromToken(token);

                        return ResponseEntity.ok(new LoginResponseDTO(token, expiration));
                    }else{
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                    }
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/register")
    public Mono<User> registerUser(@RequestBody AuthRegisterDTO authRegisterDTO) {

        return service.registerUser(authRegisterDTO);

    }
}
