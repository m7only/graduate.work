package m7.graduatework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.CrossOrigin;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@CrossOrigin(value = "http://localhost:3000")
public class WebSecurityConfig {

    private static final String[] PERMIT_ALL = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/ads",
            "/images/**",
            "/login",
            "/register"
    };
    private static final String[] PERMIT_AUTHENTICATED = {
            "/ads/**",
            "/comments/**",
            "/users/**"
    };
    private final UserDetailsService userDetailsService;

    public WebSecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(PERMIT_ALL).permitAll()
                        .requestMatchers(PERMIT_AUTHENTICATED).authenticated()
                );
        return http.build();
    }
}