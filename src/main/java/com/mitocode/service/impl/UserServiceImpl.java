package com.mitocode.service.impl;

import com.mitocode.enums.RoleEnum;
import com.mitocode.model.Role;
import com.mitocode.model.User;
import com.mitocode.repository.IGenericRepository;
import com.mitocode.repository.IRoleRepository;
import com.mitocode.repository.IUserRepository;
import com.mitocode.dto.AuthRegisterDTO;
import com.mitocode.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends CRUDImpl<User, String> implements IUserService {

    private final IUserRepository userRepo;
    private final IRoleRepository roleRepo;
    private final BCryptPasswordEncoder bcrypt;

    @Override
    protected IGenericRepository<User, String> getRepo() {
        return userRepo;
    }

    @Override
    public Mono<User> registerUser(AuthRegisterDTO authRegisterDTO) {
        return Flux.fromIterable(authRegisterDTO.roles()) // Procesamos cada rol como Flux
                .map(roleName -> {
                    RoleEnum roleEnum = RoleEnum.fromName(roleName);
                    Role role = new Role();
                    role.setId(roleEnum.getId());
                    role.setName(roleEnum.name());
                    return role;
                })
                .collectList() // Convertimos el Flux<Role> a Mono<List<Role>>
                .map(roles -> {
                    // Creamos el usuario una vez que tenemos la lista de roles
                    User user = User.builder()
                            .username(authRegisterDTO.username())
                            .password(bcrypt.encode(authRegisterDTO.password()))
                            .roles(roles)
                            .status(true)
                            .build();
                    return user;
                })
                .flatMap(userRepo::save); // Guardamos el usuario reactivamente
    }


    @Override
    public Mono<com.mitocode.security.User> searchByUser(String username) {
        return userRepo.findOneByUsername(username)
                .flatMap(user -> Flux.fromIterable(user.getRoles())
                        .flatMap(userRole -> roleRepo.findById(userRole.getId())
                                .map(Role::getName))
                        .collectList()
                        .map(roles -> new com.mitocode.security.User(user.getUsername(), user.getPassword(), user.isStatus(), roles))
                );
    }

}
