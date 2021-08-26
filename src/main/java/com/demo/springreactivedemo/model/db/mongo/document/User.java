package com.demo.springreactivedemo.model.db.mongo.document;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Document
@NoArgsConstructor
@Getter
public class User {

    private String id;
    private String name;

    public User(String name) {
        this.name = name;
    }
}
