package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private RepositoryUserDetailsService repositoryUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(repositoryUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authenticationProvider(authenticationProvider());

    http.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**")); // ✅ solo se desactiva en la API
    http.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)); // ✅ sesiones para login web

    http.authorizeHttpRequests(auth -> auth
        // PÚBLICO
        .requestMatchers("/", "/login", "/css/**", "/book/image/**", "/book/**",
                         "/basket", "/createAccount", "/addToBasket", "/api/auth/**").permitAll()

        // ADMIN
        .requestMatchers("/newBook", "/editBook", "/users").hasRole("ADMIN")

        // API REST PROTEGIDA
        .requestMatchers("/api/**").authenticated()

        // DEMÁS
        .anyRequest().authenticated()
    );

    http.formLogin(form -> form
        .loginPage("/login")
        .failureUrl("/errorNoSesion")
        .defaultSuccessUrl("/")
        .permitAll()
    );

    http.logout(logout -> logout
        .logoutUrl("/logout")
        .logoutSuccessUrl("/")
        .permitAll()
    );

    http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}

}