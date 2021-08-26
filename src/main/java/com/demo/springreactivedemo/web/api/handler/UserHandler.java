package com.demo.springreactivedemo.web.api.handler;

import com.demo.springreactivedemo.model.db.mongo.document.User;
import com.demo.springreactivedemo.repository.UserReactiveMongoRepository;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@Component
public class UserHandler {

    private UserReactiveMongoRepository userRepository;

    public UserHandler(UserReactiveMongoRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<ServerResponse> add(ServerRequest request) {
        Mono<User> user = request.bodyToMono(User.class);
        Flux<User> savedUser = userRepository.insert(user);

        return ServerResponse.ok().body(savedUser, User.class);
    }
}
