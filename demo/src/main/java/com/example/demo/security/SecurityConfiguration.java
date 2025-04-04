package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private RepositoryUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());
        http.authorizeHttpRequests(
                authorize -> authorize
                //PUBLIC
                .requestMatchers("/").permitAll()
                .requestMatchers("/css/styles.css").permitAll()
                .requestMatchers("/book/image/**").permitAll()
                .requestMatchers("/book/**").permitAll()
                .requestMatchers("/basket").permitAll()
                .requestMatchers("/createAccount").permitAll()
                .requestMatchers("/addToBasket").permitAll()
                //ADMIN
                .requestMatchers("/newBook").hasRole("ADMIN")
                .requestMatchers("/editBook").hasRole("ADMIN")
                .requestMatchers("/users").hasRole("ADMIN")
                //OTHERS
                .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin.loginPage("/login").failureUrl("/errorNoSesion")
                        .defaultSuccessUrl("/").permitAll())
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/").permitAll());
        return http.build();
    }
}
