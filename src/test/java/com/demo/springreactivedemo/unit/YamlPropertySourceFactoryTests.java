package com.demo.springreactivedemo.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;

import com.demo.springreactivedemo.config.YamlPropertySourceFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertySource;

class YamlPropertySourceFactoryTests {

    @Test
    void PropertySourceCreated() throws IOException {
        YamlPropertySourceFactory yamlPropertySourceFactory = new YamlPropertySourceFactory();

        PropertySource<?> propertySource = yamlPropertySourceFactory.createPropertySource(
            "version.yml",
            new EncodedResource(new ClassPathResource("version.yml"))
        );

        String version = String.valueOf(propertySource.getProperty("version"));

        assertEquals("SNAPSHOT", version);
    }

    @Test
    void PropertySourceNotFound() throws IOException {
        YamlPropertySourceFactory yamlPropertySourceFactory = new YamlPropertySourceFactory();

        Executable executable = () -> yamlPropertySourceFactory.createPropertySource(
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
        YamlPropertiesFactoryBean factory = Mockito.mock(YamlPropertiesFactoryBean.class);
        Mockito.when(factory.getObject()).thenReturn(null);

        YamlPropertySourceFactory yamlPropertySourceFactory = new YamlPropertySourceFactory(factory);

        Executable executable = () -> yamlPropertySourceFactory.createPropertySource(
            "version.yml",
            new EncodedResource(new ClassPathResource("version.yml"))
        );

        assertThrows(NoSuchElementException.class, executable);
    }

    @Test
    void PropertyNameIsEmpty() throws IOException {
        YamlPropertySourceFactory yamlPropertySourceFactory = new YamlPropertySourceFactory();

        Executable executable = () -> yamlPropertySourceFactory.createPropertySource(
            "",
            new EncodedResource(new ClassPathResource(""))
        );

        assertThrows(IllegalStateException.class, executable);
    }
}
