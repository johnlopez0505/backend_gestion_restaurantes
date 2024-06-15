package com.john.backend_gestion_restaurantes.configuracion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.john.backend_gestion_restaurantes.modelos.CustomLocalDateTimeDeserializer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.time.LocalDateTime;

/**
 * Configuración de Jackson para manejar fechas con Java 8 {@link LocalDateTime}.
 *
 * <p>Esta clase configura un {@link ObjectMapper} con soporte para serialización y
 * deserialización de fechas utilizando el módulo {@link JavaTimeModule}. Además,
 * registra un deserializador personalizado {@link CustomLocalDateTimeDeserializer}
 * para convertir fechas en formato personalizado.</p>
 *
 * <p>Para utilizar esta configuración, asegúrate de tener el deserializador personalizado
 * {@link CustomLocalDateTimeDeserializer} implementado correctamente y registrado en el
 * módulo de {@link JavaTimeModule}.</p>
 *
 * @see Configuration
 * @see ObjectMapper
 * @see JavaTimeModule
 * @see SimpleModule
 * @see LocalDateTime
 * @see CustomLocalDateTimeDeserializer
 * @author john-lopez
 */
@Configuration
public class JacksonConfig {

    /**
     * Configura un {@link ObjectMapper} para manejar fechas con Java 8 {@link LocalDateTime}.
     *
     * <p>Este método crea un nuevo {@link ObjectMapper} y configura el módulo
     * {@link JavaTimeModule} para manejar fechas y tiempos. También registra un
     * deserializador personalizado para convertir objetos {@link LocalDateTime}.</p>
     *
     * @return un {@link ObjectMapper} configurado para manejar fechas con Java 8
     * {@link LocalDateTime}.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new JavaTimeModule();
        module.addDeserializer(LocalDateTime.class, new  CustomLocalDateTimeDeserializer());
        mapper.registerModule(module);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}