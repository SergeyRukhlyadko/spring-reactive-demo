package com.demo.springreactivedemo.unit;

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
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;

class YamlPropertySourceFactoryTests {

    YamlPropertySourceFactory defautlYamlPropertySourceFactory = new YamlPropertySourceFactory();

    @Test
    void propertySourceCreated() throws IOException {
        PropertySource<?> propertySource = defautlYamlPropertySourceFactory.createPropertySource(
            "version.yml",
            new EncodedResource(new ClassPathResource("version.yml"))
        );

        String version = String.valueOf(propertySource.getProperty("version"));

        assertEquals("SNAPSHOT", version);
    }

    @Test
    void propertySourceNotFound() throws IOException {
        Executable executable = () -> defautlYamlPropertySourceFactory.createPropertySource(
            "version.yml",
            new EncodedResource(new ClassPathResource("property/not/exists/version.yml"))
        );

        assertThrows(FileNotFoundException.class, executable);
    }

    @Test
    void yamlPropertiesFactoryBeanIsNull() {
        Executable executable = () -> new YamlPropertySourceFactory(null);

        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void propertiesIsNull() throws IOException {
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
    void propertyNameIsEmpty() throws IOException {
        Executable executable = () -> defautlYamlPropertySourceFactory.createPropertySource(
            "",
            new EncodedResource(new ClassPathResource(""))
        );

        assertThrows(IllegalStateException.class, executable);
    }

    @Test
    void propertyNameIsNull() throws IOException {
        ClassPathResource classPathResource = mock(ClassPathResource.class);
        when(classPathResource.exists()).thenReturn(true);
        when(classPathResource.getFilename()).thenReturn(null);

        Executable executable = () -> defautlYamlPropertySourceFactory.createPropertySource(
            null,
            new EncodedResource(classPathResource)
        );

        assertThrows(IllegalStateException.class, executable);
    }
}
