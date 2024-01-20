package tm.ugur.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import tm.ugur.services.PersonDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final PersonDetailService personDetailService;


    @Autowired
    public SecurityConfig(PersonDetailService personDetailService) {
        this.personDetailService = personDetailService;
    }

    @Bean
    public UserDetailsService usersDetailService(){
        return this.personDetailService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http.csrf().disable().authorizeHttpRequests(auth ->
                auth.requestMatchers("/users/create", "/users", "/users/store").hasRole("SUPER")
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/test/**").permitAll()
                        .requestMatchers("/websocket-ugur/**").permitAll()
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
