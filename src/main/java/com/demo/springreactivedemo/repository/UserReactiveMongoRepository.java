package com.demo.springreactivedemo.repository;

import com.demo.springreactivedemo.model.db.mongo.document.User;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserReactiveMongoRepository extends ReactiveMongoRepository<User, String> {}
