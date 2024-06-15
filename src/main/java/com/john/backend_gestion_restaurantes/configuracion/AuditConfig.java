package com.john.backend_gestion_restaurantes.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
/**
 * Configuración de auditoría para JPA.
 *
 * <p>Esta clase configura la auditoría en JPA utilizando Spring Data JPA. La anotación
 * {@link EnableJpaAuditing} habilita la funcionalidad de auditoría, y el método {@code auditorAware}
 * define el bean que proporciona el auditor actual.</p>
 *
 * <p>Para usar esta configuración, asegúrate de que la clase {@link AuditorAwareImpl} esté
 * correctamente implementada y que el contexto de seguridad de Spring esté configurado para
 * proporcionar la autenticación necesaria.</p>
 *
 * @see Configuration
 * @see EnableJpaAuditing
 * @see AuditorAware
 * @see AuditorAwareImpl
 * @author john-lopez
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuditConfig {

    /**
     * Define el bean {@link AuditorAware} que proporciona el auditor actual.
     *
     * <p>Este método registra una instancia de {@link AuditorAwareImpl} como un bean en el
     * contexto de la aplicación de Spring. Esta instancia se utilizará para obtener el auditor
     * actual en las entidades auditables.</p>
     *
     * @return una instancia de {@link AuditorAwareImpl} que proporciona el auditor actual.
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl();
    }

}
