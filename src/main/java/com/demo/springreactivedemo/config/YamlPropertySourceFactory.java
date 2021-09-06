package com.demo.springreactivedemo.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class YamlPropertySourceFactory extends DefaultPropertySourceFactory {

    /**
     * Create {@link org.springframework.core.env.PropertySource} from yaml configuration resourse.
     * Throw {@link java.io.FileNotFoundException} is necessary
     * for correcly work of ignoreResourceNotFound property in
     * {@link org.springframework.context.annotation.PropertySource} annotation.
     * @param name
     *        {@link java.lang.String} resource name
     * @param resource
     *        {@link org.springframework.core.io.support.EncodedResource} contains yaml properties
     */
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {

        Resource propertyResource = resource.getResource();

        if (propertyResource.exists()) {
            YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
            factory.setResources(propertyResource);

            Properties properties = Optional.ofNullable(factory.getObject()).orElseThrow();

            if (name == null || name.isBlank()) {
                name = propertyResource.getFilename();
                if (name == null || name.isBlank()) {
                    throw new IllegalStateException(propertyResource.getDescription() + " file name does not exist");
                }
            }

            return new PropertiesPropertySource(name, properties);
        } else {
            if (log.isWarnEnabled()) {
                log.warn("{} does not exist", propertyResource.getDescription());
            }

            throw new FileNotFoundException(propertyResource.getDescription() + " does not exist");
        }
    }
}
