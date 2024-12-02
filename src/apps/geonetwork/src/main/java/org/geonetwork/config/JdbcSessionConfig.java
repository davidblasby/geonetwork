/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.session.config.SessionRepositoryCustomizer;
import org.springframework.session.jdbc.JdbcIndexedSessionRepository;

// See https://docs.spring.io/spring-session/reference/configuration/jdbc.html#session-attributes-as-json

/**
 * setup JDBC Sessions so they read/write in JSON format (jsonb in postgresql). 1. make sure you have spring JDBC
 * sessions setup (cf application.yaml) 2. make sure to change the SPRING_SESSION_ATTRIBUTES column "ATTRIBUTE_BYTES"
 * from type "bytea" to "jsonb" + see schema-postgresql-jdbc.sql 3. this will configure: a.
 * "springSessionConversionService" bean to configure the conversion b. modify the load/save SQL to be postgresql jsonb
 * aware c. add trivial JsonDeserializer/JsonSerializer to do the conversion
 */
@Configuration
public class JdbcSessionConfig implements BeanClassLoaderAware {

    private ClassLoader classLoader;

    @Bean("springSessionConversionService")
    public GenericConversionService springSessionConversionService(ObjectMapper objectMapper) {
        ObjectMapper copy = objectMapper.copy();
        // Register Spring Security Jackson Modules
        copy.registerModules(SecurityJackson2Modules.getModules(this.classLoader));
        // Activate default typing explicitly if not using Spring Security
        // copy.activateDefaultTyping(copy.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL,
        // JsonTypeInfo.As.PROPERTY);
        GenericConversionService converter = new GenericConversionService();
        converter.addConverter(Object.class, byte[].class, new SerializingConverter(new JsonSerializer(copy)));
        converter.addConverter(byte[].class, Object.class, new DeserializingConverter(new JsonDeserializer(copy)));
        return converter;
    }

    private static final String CREATE_SESSION_ATTRIBUTE_QUERY =
            """
            INSERT INTO %TABLE_NAME%_ATTRIBUTES (SESSION_PRIMARY_ID, ATTRIBUTE_NAME, ATTRIBUTE_BYTES)
            VALUES (?, ?, encode(?, 'escape')::jsonb)
            """;

    private static final String UPDATE_SESSION_ATTRIBUTE_QUERY =
            """
            UPDATE %TABLE_NAME%_ATTRIBUTES
            SET ATTRIBUTE_BYTES = encode(?, 'escape')::jsonb
            WHERE SESSION_PRIMARY_ID = ?
            AND ATTRIBUTE_NAME = ?
            """;

    @Bean
    SessionRepositoryCustomizer<JdbcIndexedSessionRepository> customizer() {
        return (sessionRepository) -> {
            sessionRepository.setCreateSessionAttributeQuery(CREATE_SESSION_ATTRIBUTE_QUERY);
            sessionRepository.setUpdateSessionAttributeQuery(UPDATE_SESSION_ATTRIBUTE_QUERY);
        };
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    static class JsonSerializer implements Serializer<Object> {

        private final ObjectMapper objectMapper;

        JsonSerializer(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public void serialize(Object object, OutputStream outputStream) throws IOException {
            this.objectMapper.writeValue(outputStream, object);
        }
    }

    static class JsonDeserializer implements Deserializer<Object> {

        private final ObjectMapper objectMapper;

        JsonDeserializer(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public Object deserialize(InputStream inputStream) throws IOException {
            return this.objectMapper.readValue(inputStream, Object.class);
        }
    }
}
