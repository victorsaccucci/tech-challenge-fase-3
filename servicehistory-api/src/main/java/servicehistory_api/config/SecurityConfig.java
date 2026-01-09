package servicehistory_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        try {
            http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                    // Endpoints públicos
                    .requestMatchers("/actuator/**", "/h2-console/**").permitAll()
                    
                    // GraphQL - Controle de acesso será feito no interceptor
                    .requestMatchers("/graphql").permitAll()
                    
                    .anyRequest().authenticated()
                );
            
            return http.build();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao configurar segurança: " + e.getMessage(), e);
        }
    }
    
    @Bean
    public WebGraphQlInterceptor authInterceptor(JwtUtil jwtUtil) {
        return new GraphQLAuthInterceptor(jwtUtil);
    }
}