package com.fady.serie_episode.security;

import java.util.Collections;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.csrf(csrf -> csrf.disable())
				.cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
					@Override
					public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
						CorsConfiguration cors = new CorsConfiguration();
						cors.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
						cors.setAllowedMethods(Collections.singletonList("*"));
						cors.setAllowedHeaders(Collections.singletonList("*"));
						cors.setExposedHeaders(Collections.singletonList("Authorization"));
						return cors;
					}
				}))
				.authorizeHttpRequests(requests -> requests
						// Consulter tous les épisodes, séries ou par ID / recherche : ADMIN et USER
						.requestMatchers(HttpMethod.GET, "/api", "/api/**").hasAnyAuthority("ADMIN", "USER")
						.requestMatchers(HttpMethod.GET, "/series", "/series/**").hasAnyAuthority("ADMIN", "USER")
						.requestMatchers(HttpMethod.GET, "/rest", "/rest/**").hasAnyAuthority("ADMIN", "USER")

						// Ajouter, Modifier, Supprimer un épisode ou une série : ADMIN uniquement
						.requestMatchers(HttpMethod.POST, "/api", "/api/**").hasAuthority("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/api", "/api/**").hasAuthority("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/api", "/api/**").hasAuthority("ADMIN")

						.requestMatchers(HttpMethod.POST, "/series", "/series/**").hasAuthority("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/series", "/series/**").hasAuthority("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/series", "/series/**").hasAuthority("ADMIN")

						.requestMatchers(HttpMethod.POST, "/rest", "/rest/**").hasAuthority("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/rest", "/rest/**").hasAuthority("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/rest", "/rest/**").hasAuthority("ADMIN")

						// Tout le reste nécessite une authentification
						.anyRequest().authenticated())
				.addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}
