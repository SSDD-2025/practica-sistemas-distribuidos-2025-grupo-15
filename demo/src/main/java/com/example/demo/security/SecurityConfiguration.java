package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.security.jwt.JwtRequestFilter;
import com.example.demo.security.jwt.UnauthorizedHandlerJwt;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	RepositoryUserDetailsService userDetailsService;

	@Autowired
	private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	@Order(1)
	public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
		
		http.authenticationProvider(authenticationProvider());
		
		http
			.securityMatcher("/api/**")
			.exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));
		
		http
			.authorizeHttpRequests(authorize -> authorize
                    // PRIVATE ENDPOINTS
                    .requestMatchers(HttpMethod.POST,"/api/books/").hasRole("USER")
                    .requestMatchers(HttpMethod.PUT,"/api/books/**").hasRole("USER")
                    .requestMatchers(HttpMethod.DELETE,"/api/books/**").hasRole("ADMIN")
					.requestMatchers(HttpMethod.POST,"/api/users/").hasRole("USER")
                    .requestMatchers(HttpMethod.PUT,"/api/users/**").hasRole("USER")
                    .requestMatchers(HttpMethod.DELETE,"/api/users/**").hasRole("ADMIN")
					.requestMatchers(HttpMethod.POST,"/api/purchase/").hasRole("USER")
                    .requestMatchers(HttpMethod.PUT,"/api/purchase/**").hasRole("USER")
                    .requestMatchers(HttpMethod.DELETE,"/api/purchase/**").hasRole("ADMIN")
					.requestMatchers(HttpMethod.POST,"/api/reviews/").hasRole("USER")
                    .requestMatchers(HttpMethod.PUT,"/api/reviews/**").hasRole("USER")
                    .requestMatchers(HttpMethod.DELETE,"/api/reviews/**").hasRole("ADMIN")
					// PUBLIC ENDPOINTS
					.anyRequest().permitAll()
			);
		
        // Disable Form login Authentication
        http.formLogin(formLogin -> formLogin.disable());

        // Disable CSRF protection (it is difficult to implement in REST APIs)
        http.csrf(csrf -> csrf.disable());
		

        // Disable Basic Authentication
        http.httpBasic(httpBasic -> httpBasic.disable());

        // Stateless session
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// Add JWT Token filter
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	@Order(1)
	public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {

		http.authenticationProvider(authenticationProvider());

		http
				.authorizeHttpRequests(authorize -> authorize
						// PUBLIC PAGES
						.requestMatchers("/").permitAll()
						.requestMatchers("/css/**").permitAll()
						.requestMatchers("/images/**").permitAll() // Allow access to static resources
						.requestMatchers("/book/**").permitAll()
						.requestMatchers("/error").permitAll()
						.requestMatchers("/createAccount").permitAll()
						.requestMatchers("/basket").permitAll()
						.requestMatchers("/addToBasket").permitAll()
						// PRIVATE PAGES
						.requestMatchers("/newBook").hasAnyRole("ADMIN")
						.requestMatchers("/editBook").hasAnyRole("ADMIN")
						.requestMatchers("/saveEdit").hasAnyRole("ADMIN")
						.requestMatchers("/deleteBook/**").hasAnyRole("ADMIN")
						.requestMatchers("/users").hasAnyRole("ADMIN")
						.requestMatchers("/profile").hasAnyRole("USER")
						.requestMatchers("/editProfile").hasAnyRole("USER")
						.requestMatchers("/myPurchases").hasAnyRole("USER")
						.requestMatchers("/deletePurchase/**").hasAnyRole("USER")
						.requestMatchers("/newReview").hasAnyRole("USER")
						.requestMatchers("/myReviews").hasAnyRole("USER")
						.requestMatchers("/deleteReview/**").hasAnyRole("USER")
						
						.requestMatchers("/saveEditProfile").hasAnyRole("USER")

						// OpenAPI
						.requestMatchers("/v3/api-docs*/**").permitAll()
						.requestMatchers("/swagger-ui.html").permitAll()
						.requestMatchers("/swagger-ui/**").permitAll()
				)
				.formLogin(formLogin -> formLogin
						.loginPage("/login")
						.failureUrl("/loginerror")
						.defaultSuccessUrl("/")
						.permitAll()
				)
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/")
						.permitAll()
				);

		return http.build();
	}
}