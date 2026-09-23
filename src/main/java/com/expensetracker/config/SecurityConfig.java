package com.expensetracker.config;

import com.expensetracker.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
<<<<<<< HEAD
import org.springframework.security.core.Authentication;
=======
>>>>>>> ddc854d9aa2bb888f13a42a657cf261d844435f8
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return (request, response, authentication) -> {
            boolean admin = authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
            response.sendRedirect(admin ? "/admin/dashboard" : "/dashboard");
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CustomUserDetailsService userDetailsService,
            AuthenticationSuccessHandler loginSuccessHandler
    ) throws Exception {
        http
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(auth -> auth
<<<<<<< HEAD
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register",
                                "/health",
                                "/css/**",
                                "/images/**"
                        ).permitAll()
=======
                        .requestMatchers("/", "/login", "/register", "/health", "/css/**", "/images/**").permitAll()
>>>>>>> ddc854d9aa2bb888f13a42a657cf261d844435f8
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/dashboard", "/expenses/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(loginSuccessHandler)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
<<<<<<< HEAD
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/access-denied")
                );
=======
                .exceptionHandling(exception -> exception.accessDeniedPage("/access-denied"));
>>>>>>> ddc854d9aa2bb888f13a42a657cf261d844435f8

        return http.build();
    }
}
