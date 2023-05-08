package com.intraviologistica.intravio.config;

import com.intraviologistica.intravio.model.enums.Perfil;
import com.intraviologistica.intravio.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    private static final String ADMIN = Perfil.ADMIN.name();
    private static final String STANDARD = Perfil.STANDARD.name();

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    private final static String[] PUBLIC_MATCHERS = {
            "/api/v1/usuarios/autenticar",
            "/api/v1/pedidos/rastreio/**",
    };

    private final static String[] ADMIN_MATCHERS = {
            "/api/v1/usuarios/cadastrar",
            "/api/v1/filiais/**"
    };

    private final static String[] STANDARD_MATCHERS = {
            "/api/v1/departamentos/**",
            "/api/v1/funcionarios/**",
            "/api/v1/pedidos/**",
            "/api/v1/produtos/**",
            "/api/v1/romaneios/**",
            "/api/v1/transportadores/**",
    };

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeHttpRequests
                        ((auth) -> {
                                    try {
                                        auth
                                                /* Acesso público */
                                                .requestMatchers(PUBLIC_MATCHERS).permitAll()
                                                /* Acesso apenas para ADMIN */
                                                .requestMatchers(ADMIN_MATCHERS).hasAnyAuthority(ADMIN)
                                                /* Acesso apenas para STARDAND e ADMIN */
                                                .requestMatchers(STANDARD_MATCHERS).hasAnyAuthority(ADMIN, STANDARD)
                                                .anyRequest().authenticated()
                                                .and()
                                                .sessionManagement()
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                                .and()
                                                .authenticationProvider(authenticationProvider)
                                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                        );
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
