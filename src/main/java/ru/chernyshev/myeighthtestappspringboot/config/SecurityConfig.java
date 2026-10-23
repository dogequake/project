package ru.chernyshev.myeighthtestappspringboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(auth -> auth
                .antMatchers("/login", "/register", "/css/**", "/error").permitAll()
                // Actuator: health/info открыты, остальное только ADMIN
                .antMatchers("/actuator/health", "/actuator/health/**", "/actuator/info").permitAll()
                .antMatchers("/actuator/**", "/h2-console/**", "/admin/**").hasRole("ADMIN")
                // Создание / изменение / удаление: USER и ADMIN (READ_ONLY - нет)
                .antMatchers("/servers/new", "/servers/*/edit",
                             "/locations/new", "/locations/*/edit").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/servers/**", "/locations/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll())
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll())
            // консоль H2 не умеет работать с CSRF-токеном и использует frame
            .csrf(csrf -> csrf.ignoringAntMatchers("/h2-console/**"))
            .headers(headers -> headers.frameOptions().sameOrigin());
        return http.build();
    }
}
