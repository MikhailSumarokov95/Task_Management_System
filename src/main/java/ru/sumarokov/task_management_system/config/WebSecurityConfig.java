package ru.sumarokov.task_management_system.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@ConditionalOnProperty(name = "security.enabled", havingValue = "true")
public class WebSecurityConfig {

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.
//                authorizeHttpRequests((request) -> request
//                        .requestMatchers("/register", "/login")
//                        .permitAll()
//                        .anyRequest().hasAnyRole("ADMIN",
//                                "CREDIT_SPECIALIST"))
//                .formLogin((form) -> form
//                        .loginPage("/login")
//                        .loginProcessingUrl("/perform-login")
//                        .defaultSuccessUrl("/", true)
//                        .failureUrl("/login?error")
//                        .usernameParameter("username")
//                        .passwordParameter("password")
//                        .permitAll())
//                .logout((logout) -> logout.logoutSuccessUrl("/login"));
//        return http.build();
//    }

//    @Bean
//    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService);
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        return authenticationProvider;
//    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
