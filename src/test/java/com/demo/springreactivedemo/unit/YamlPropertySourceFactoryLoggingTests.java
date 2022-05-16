package com.demo.springreactivedemo.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.demo.springreactivedemo.config.YamlPropertySourceFactory;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.ClassPathResource;

class YamlPropertySourceFactoryLoggingTests {

    YamlPropertySourceFactory defautlYamlPropertySourceFactory = new YamlPropertySourceFactory();

    static ListAppender<ILoggingEvent> appender = new ListAppender<>();
    static Logger testLogger = (Logger) LoggerFactory.getLogger(YamlPropertySourceFactory.class);

    @BeforeAll
    static void beforeAll() {
        testLogger.addAppender(appender);
    }

    @BeforeEach
    void beforeEach() {
        testLogger.setLevel(Level.TRACE);
        appender.start();
    }

    @AfterEach
    void afterEach() {
        appender.list.clear();
    }

    @AfterAll
    static void afterAll() {
        appender.stop();
        testLogger.detachAppender(appender);
    }

    @Test
    void fileNotFoundWarnLoggingEnabled() throws IOException {
        try {
            defautlYamlPropertySourceFactory.createPropertySource(
                "notexists.yml",
                new EncodedResource(new ClassPathResource("notexists.yml"))
            );
        } catch(FileNotFoundException e) {}

        assertThat(appender.list)
            .extracting(ILoggingEvent::getFormattedMessage)
            .containsExactly("class path resource [notexists.yml] does not exist");
    }

    @Test
    void fileNotFoundWarnLoggingDisabled() throws IOException {
        testLogger.setLevel(Level.OFF);

        try {
            defautlYamlPropertySourceFactory.createPropertySource(
                "notexists.yml",
                new EncodedResource(new ClassPathResource("notexists.yml"))
            );
        } catch(FileNotFoundException e) {}

        assertEquals(0, appender.list.size());
    }
}
