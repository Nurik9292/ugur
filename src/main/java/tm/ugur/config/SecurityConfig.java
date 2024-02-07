package tm.ugur.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tm.ugur.services.details.PersonDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final PersonDetailService personDetailService;
    private final JWTFilter jwtFilter;


    @Autowired
    public SecurityConfig(PersonDetailService personDetailService, JWTFilter jwtFilter) {
        this.personDetailService = personDetailService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public UserDetailsService usersDetailService(){
        return this.personDetailService;
    }


    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        return http.securityMatcher("/api/**").csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                    auth
                        .requestMatchers("/api/auth/registration").permitAll()
                        .anyRequest().permitAll())
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(this.jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http.csrf(Customizer.withDefaults())
                .authorizeHttpRequests(auth ->
                    auth
                        .requestMatchers("/users/create", "/users", "/users/store").hasRole("SUPER")
                        .requestMatchers("/test").permitAll()
                        .requestMatchers("/websocket-ugur", "/websocket-ugur/**").permitAll()
                        .requestMatchers("/topic", "/topic/mobile", "/topic/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(usersDetailService());
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
