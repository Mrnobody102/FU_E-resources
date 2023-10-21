package fpt.edu.eresourcessystem.config.security;

import fpt.edu.eresourcessystem.enums.AccountEnum;
import fpt.edu.eresourcessystem.service.security.CustomizeUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private CustomizeUserDetailsService customizeUserDetailsService;

    private CustomizeAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    public SecurityConfig(CustomizeUserDetailsService customizeUserDetailsService, CustomizeAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.customizeUserDetailsService = customizeUserDetailsService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(customizeUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        // Disable  csrf
        http.csrf(AbstractHttpConfigurer::disable);

        // Authentication
        http.formLogin(auth -> auth.loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(customAuthenticationSuccessHandler)
                .failureUrl("/login?error"));
        http.logout(auth -> auth.logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout"));

        // Authorization
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/home", "/guest", "/login", "/css/**", "/js/**", "/images/**", "/assets/**").permitAll()
                        .requestMatchers("/librarian/**").hasAnyRole(AccountEnum.Role.LIBRARIAN.name())
                        .requestMatchers("/lecturer/**").hasAnyRole(AccountEnum.Role.LECTURER.name())
                        .requestMatchers("/student/**").hasAnyRole(AccountEnum.Role.STUDENT.name())
                        .anyRequest().authenticated()

                )
        ;
        // Exception Handling


        return http.build();
    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails librarian = User.withDefaultPasswordEncoder()
//                .username("librarian")
//                .password("password")
//                .roles(AccountEnum.Role.LIBRARIAN.name())
//                .build();
//
//        UserDetails lecturer = User.withDefaultPasswordEncoder()
//                .username("lecturer")
//                .password("password")
//                .roles(AccountEnum.Role.LECTURER.name())
//                .build();
//
//        UserDetails student = User.withDefaultPasswordEncoder()
//                .username("student")
//                .password("password")
//                .roles(AccountEnum.Role.STUDENT.name())
//                .build();
//
//        return new InMemoryUserDetailsManager(librarian, lecturer, student);
//    }
}