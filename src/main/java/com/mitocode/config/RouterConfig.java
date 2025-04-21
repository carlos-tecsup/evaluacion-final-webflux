package com.mitocode.config;

import com.mitocode.handler.AuthHandler;
import com.mitocode.handler.CourseHandler;
import com.mitocode.handler.StudentHandler;
import com.mitocode.handler.TuitionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> studentsRoutes(StudentHandler handler) {
        return route(GET("/v2/students"), handler::findAllSortedByAge)
                .andRoute(POST("/v2/students/create"), handler::save)
                .andRoute(PUT("/v2/students/{id}"), handler::update)
                .andRoute(DELETE("/v2/students/{id}"), handler::delete);
    }

    @Bean
    public RouterFunction<ServerResponse> coursesRoutes(CourseHandler handler) {
        return route(GET("/v2/courses"), handler::findAll)
                .andRoute(POST("/v2/courses/create"), handler::save)
                .andRoute(PUT("/v2/courses/{id}"), handler::update)
                .andRoute(DELETE("/v2/courses/{id}"), handler::delete);
    }

    @Bean
    public RouterFunction<ServerResponse> tuitionRoutes(TuitionHandler handler) {
        return route(POST("/v2/tuition/create"), handler::save);
    }

    @Bean
    public RouterFunction<ServerResponse> authRoutes(AuthHandler handler) {
        return route(POST("v2/login"), handler::login)
                .andRoute(POST("v2/register"), handler::register);
    }
}
