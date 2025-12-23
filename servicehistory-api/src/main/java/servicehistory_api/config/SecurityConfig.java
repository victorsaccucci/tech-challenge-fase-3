package servicehistory_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos
                .requestMatchers("/actuator/**", "/h2-console/**").permitAll()
                
                // GraphQL - Médicos podem visualizar e editar, Enfermeiros podem registrar e acessar
                // Pacientes podem visualizar apenas os seus próprios registros
                .requestMatchers("/graphql").permitAll() // Controle de acesso será feito no nível do resolver
                
                .anyRequest().authenticated()
            );
        
        return http.build();
    }
}