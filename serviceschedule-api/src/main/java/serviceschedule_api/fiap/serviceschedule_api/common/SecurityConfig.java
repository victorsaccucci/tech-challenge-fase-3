package serviceschedule_api.fiap.serviceschedule_api.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos
                .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/actuator/**").permitAll()
                
                // CRUD de agendamentos - Médicos e Enfermeiros podem criar/editar, Pacientes podem visualizar os seus
                .requestMatchers("/api/appointments").hasAnyAuthority("DOCTOR", "NURSE")
                .requestMatchers("/api/appointments/*/status").hasAnyAuthority("DOCTOR", "NURSE")
                .requestMatchers("/api/appointments/patient/**").hasAnyAuthority("DOCTOR", "NURSE", "PATIENT")
                .requestMatchers("/api/appointments/doctor/**").hasAnyAuthority("DOCTOR", "NURSE")
                
                // Listagem de médicos - Médicos e Enfermeiros
                .requestMatchers("/api/users/doctors").hasAnyAuthority("DOCTOR", "NURSE")
                
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}