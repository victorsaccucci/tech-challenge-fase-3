package cuidei_api.fiap.cuidei_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(auth -> auth
                        //Adaptar para todas as URLs
                        .requestMatchers(HttpMethod.GET, "/api/v1/patient").hasAnyRole("PATIENT")
                        .requestMatchers(HttpMethod.GET, "/api/v1/doctor").hasAnyRole("DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/api/v1/nurse").hasAnyRole("NURSE")
                ).httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {

        UserDetails doctor = User.builder()
                .username("doc")
                .password(passwordEncoder.encode("123"))
                .roles("DOCTOR")
                .build();

        UserDetails nurse = User.builder()
                .username("nurse")
                .password(passwordEncoder.encode("123"))
                .roles("NURSE")
                .build();

        UserDetails patient = User.builder()
                .username("patient")
                .password(passwordEncoder.encode("123"))
                .roles("PATIENT")
                .build();

        return new InMemoryUserDetailsManager(doctor, nurse, patient);
    }
}

