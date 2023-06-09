package ch.clip.trips;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    public static final String AUTHORITIES_CLAIM_NAME = "roles";

    private final PasswordEncoder passwordEncoder;

    public WebSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .oauth2ResourceServer((server) -> server.jwt(jwt -> jwt.jwtAuthenticationConverter(
                        authenticationConverter())))
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .sessionManagement((sess) -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(configurer -> configurer.requestMatchers(HttpMethod.POST, "/login")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .build();
    }

    @Bean
    protected UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        UserDetails user1 = User.withUsername("user1")
                .authorities("ADMIN", "STAFF_MEMBER")
                .passwordEncoder(passwordEncoder::encode)
                .password("1234")
                .build();
        manager.createUser(user1);

        UserDetails user2 = User.withUsername("user2")
                .authorities("STAFF_MEMBER")
                .passwordEncoder(passwordEncoder::encode)
                .password("1234")
                .build();
        manager.createUser(user2);

        UserDetails user3 = User.withUsername("user3")
                .authorities("ASSISTANT_MANAGER", "STAFF_MEMBER")
                .passwordEncoder(passwordEncoder::encode)
                .password("1234")
                .build();
        manager.createUser(user3);

        UserDetails user4 = User.withUsername("user4")
                .authorities("MANAGER", "STAFF_MEMBER")
                .passwordEncoder(passwordEncoder::encode)
                .password("1234")
                .build();
        manager.createUser(user4);

        return manager;
    }

    protected JwtAuthenticationConverter authenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("");
        authoritiesConverter.setAuthoritiesClaimName(AUTHORITIES_CLAIM_NAME);

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }
}