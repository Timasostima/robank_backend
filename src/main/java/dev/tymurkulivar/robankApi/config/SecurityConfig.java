package dev.tymurkulivar.robankApi.config;

import dev.tymurkulivar.robankApi.filters.FirebaseAuthenticationFilter;
import dev.tymurkulivar.robankApi.services.FirebaseAuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final FirebaseAuthService firebaseAuthService;

    public SecurityConfig(FirebaseAuthService firebaseAuthService) {
        this.firebaseAuthService = firebaseAuthService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/public/**").permitAll() // Allow public endpoints
                        .anyRequest().authenticated() // Protect all other endpoints
                )
                .addFilterBefore(new FirebaseAuthenticationFilter(firebaseAuthService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
