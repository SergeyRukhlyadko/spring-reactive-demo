package com.demo.springreactivedemo;

import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

import com.demo.springreactivedemo.model.db.mongo.document.User;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureWebTestClient
@AutoConfigureRestDocs(outputDir = "build/snippets", uriPort = 80)
class SpringReactiveDemoApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void getVersion() {
        webTestClient
            .get().uri("/api/app/version")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class).value(Matchers.startsWith("Application version: "))
            .consumeWith(document("version"));
    }

    @Test
    void addUser() {
        webTestClient
            .post().uri("/api/user")
            .body(Mono.just(new User("Tom")), User.class)
            .exchange()
            .expectStatus().isOk()
            .returnResult(User.class).getResponseBody().log();
    }
}
