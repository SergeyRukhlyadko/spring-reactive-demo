package com.demo.springreactivedemo.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

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
        try {
            YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
            factory.setResources(resource.getResource());

            Properties properties = factory.getObject();

            String propertySourceName =  Optional.ofNullable(name).orElse(resource.getResource().getFilename());

            return new PropertiesPropertySource(propertySourceName, properties);
        } catch (IllegalStateException e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }
}
