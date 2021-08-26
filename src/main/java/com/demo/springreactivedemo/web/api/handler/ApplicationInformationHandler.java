package com.demo.springreactivedemo.web.api.handler;

import java.util.Optional;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
public class ApplicationInformationHandler {

    private Environment env;

    public ApplicationInformationHandler(Environment env) {
        this.env = env;
    }

    /**
     * Return app version.
     * Default version is SNAPSHOT.
     * @param request
     *        request not used, needed for
     *        {@link org.springframework.web.reactive.function.server.HandlerFunction}
     * @return server response with app version in body
     */
    public Mono<ServerResponse> version(ServerRequest request) {
        String v = Optional.ofNullable(env.getProperty("version")).orElse("SNAPSHOT");

        return ServerResponse.ok().bodyValue("Application version: " + v);
    }
}
