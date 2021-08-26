package com.demo.springreactivedemo.web.api.router;

import com.demo.springreactivedemo.web.api.handler.ApplicationInformationHandler;
import com.demo.springreactivedemo.web.api.handler.UserHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfiguration {

    @Bean
    public RouterFunction<ServerResponse> rootRoute(
        ApplicationInformationHandler handler,
        UserHandler userHandler
    ) {
        return route()
            .GET("/api/app/version", handler::version)
            .POST("/api/user", userHandler::add)
            .build();
    }
}
