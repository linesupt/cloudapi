package com.lineying.factory;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class YamlPropertySourceFactory implements PropertySourceFactory {

    @NotNull
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        String filename = resource.getResource().getFilename();
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource.getResource());
        Properties properties = factory.getObject();
        return new PropertiesPropertySource(Objects.requireNonNull(filename), Objects.requireNonNull(properties));
    }

}