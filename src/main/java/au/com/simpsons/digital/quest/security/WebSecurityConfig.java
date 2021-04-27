package au.com.simpsons.digital.quest.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Web Security class to implement security for all the input Api's . 
 * @author Ramesh
 */
@EnableWebFluxSecurity
public class WebSecurityConfig {
	/*
    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
       UserDetails user = User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("USER")
            .build();

        return new MapReactiveUserDetailsService(user);
    }
	 */

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		//http.authorizeExchange().anyExchange().permitAll();

		http
		.csrf().disable()
		.authorizeExchange()
		.pathMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**").permitAll()
		.anyExchange().authenticated()
		.and()
		.httpBasic().and()
		.formLogin();

		return http.build();
	}

}