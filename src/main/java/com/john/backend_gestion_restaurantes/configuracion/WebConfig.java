package com.john.backend_gestion_restaurantes.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de CORS para la aplicación Spring MVC.
 *
 * <p>Esta clase configura la política de CORS (Cross-Origin Resource Sharing) para permitir
 * peticiones desde cualquier origen (*), con todos los encabezados y métodos HTTP especificados.</p>
 *
 * <p>Este configurador permite peticiones desde cualquier origen (*), con todos los encabezados
 * permitidos (*) y los métodos HTTP GET, PUT, POST, DELETE y PATCH habilitados. Asegúrate de
 * ajustar las configuraciones de CORS según las políticas de seguridad de tu aplicación.</p>
 *
 * @see Configuration
 * @see WebMvcConfigurer
 * @see CorsRegistry
 * @author john-lopez
 */
@Configuration
public class WebConfig implements WebMvcConfigurer
{

    /**
     * Configura la política de CORS para la aplicación.
     *
     * <p>Este método devuelve un {@link WebMvcConfigurer} que configura la política de CORS
     * permitiendo peticiones desde cualquier origen (*), con todos los encabezados y métodos
     * HTTP especificados.</p>
     *
     * @return un {@link WebMvcConfigurer} configurado para manejar la política de CORS.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer()
    {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("GET", "PUT", "POST", "DELETE", "PATCH");
            }
        };
    }

}

