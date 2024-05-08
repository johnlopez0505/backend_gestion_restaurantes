package com.john.backend_gestion_restaurantes.seguridad;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import com.john.backend_gestion_restaurantes.seguridad.jwt.access.JwtAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final AccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        


        // Versión 1
        /*
        AuthenticationManager authenticationManager =
                authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                        .and().build();
        */

        // Versión 2
        AuthenticationManager authenticationManager =
            authenticationManagerBuilder.authenticationProvider(authenticationProvider())
                    .build();

        return authenticationManager;

    }



    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setHideUserNotFoundExceptions(false);

        return authenticationProvider;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/webjars/**", "/img/**", "/js/**", 
                        "/api/auth/register", "/api/auth/login", "/api/refreshtoken")
                        .permitAll()
                        .requestMatchers("/api/restaurantes/**","/api/menus/**",
                        "/api/usuarios/**", "/api/reservas/**")
                        .hasAnyRole("ADMIN","USUARIO")
                        .requestMatchers("/api/auth/register/admin")
                        .hasRole("ADMIN")
                        .anyRequest().authenticated() 
                ) 
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))
                        .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin((formLogin) -> formLogin
                        .permitAll()
                ).rememberMe(
                Customizer.withDefaults()
                ).logout((logout) -> logout
                        .invalidateHttpSession(true)
                        .logoutSuccessUrl("/")
                        // .deleteCookies("JSESSIONID") // no es necesario, JSESSIONID se hace por defecto
                        .permitAll()                                

                ).csrf((protection) -> protection
                .disable())
                .cors(Customizer.withDefaults());
        
                http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                http.headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin));

        return http.build();
    }

}
