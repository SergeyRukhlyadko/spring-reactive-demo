package com.demo.springreactivedemo.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;

import com.demo.springreactivedemo.config.YamlPropertySourceFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

class YamlPropertySourceFactoryTests {

    YamlPropertySourceFactory defautlYamlPropertySourceFactory = new YamlPropertySourceFactory();

    @Test
    void PropertySourceCreated() throws IOException {
        PropertySource<?> propertySource = defautlYamlPropertySourceFactory.createPropertySource(
            "version.yml",
            new EncodedResource(new ClassPathResource("version.yml"))
        );

        String version = String.valueOf(propertySource.getProperty("version"));

        assertEquals("SNAPSHOT", version);
    }

    @Test
    void PropertySourceNotFound() throws IOException {
        Executable executable = () -> defautlYamlPropertySourceFactory.createPropertySource(
            "version.yml",
            new EncodedResource(new ClassPathResource("property/not/exists/version.yml"))
        );

        assertThrows(FileNotFoundException.class, executable);
    }

    @Test
    void YamlPropertiesFactoryBeanIsNull() {
        Executable executable = () -> new YamlPropertySourceFactory(null);

        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void PropertiesIsNull() throws IOException {
        YamlPropertiesFactoryBean factory = mock(YamlPropertiesFactoryBean.class);
        when(factory.getObject()).thenReturn(null);

        YamlPropertySourceFactory yamlPropertySourceFactory = new YamlPropertySourceFactory(factory);

        Executable executable = () -> yamlPropertySourceFactory.createPropertySource(
            "version.yml",
            new EncodedResource(new ClassPathResource("version.yml"))
        );

        assertThrows(NoSuchElementException.class, executable);
    }

    @Test
    void PropertyNameIsEmpty() throws IOException {
        Executable executable = () -> defautlYamlPropertySourceFactory.createPropertySource(
            "",
            new EncodedResource(new ClassPathResource(""))
        );

        assertThrows(IllegalStateException.class, executable);
    }

    @Test
    void PropertyNameIsNull() throws IOException {
        ClassPathResource classPathResource = mock(ClassPathResource.class);
        when(classPathResource.exists()).thenReturn(true);
        when(classPathResource.getFilename()).thenReturn(null);

        Executable executable = () -> defautlYamlPropertySourceFactory.createPropertySource(
            null,
            new EncodedResource(classPathResource)
        );

        assertThrows(IllegalStateException.class, executable);
    }

    @Test
    void FileNotFoundWarnLogging() throws IOException {
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        Logger testLogger = (Logger) LoggerFactory.getLogger(YamlPropertySourceFactory.class);
        testLogger.addAppender(appender);
        appender.start();

        try {
            defautlYamlPropertySourceFactory.createPropertySource(
                "notexists.yml",
                new EncodedResource(new ClassPathResource("notexists.yml"))
            );
        } catch(FileNotFoundException e) {}

        assertThat(appender.list)
            .extracting(ILoggingEvent::getFormattedMessage)
            .containsExactly("class path resource [notexists.yml] does not exist");

        appender.stop();
        testLogger.detachAppender(appender);
    }
}
