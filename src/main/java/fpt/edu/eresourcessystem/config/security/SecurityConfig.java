package fpt.edu.eresourcessystem.config.security;

import fpt.edu.eresourcessystem.enums.AccountEnum;
import fpt.edu.eresourcessystem.service.security.CustomizeUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import org.springframework.web.servlet.resource.VersionResourceResolver;

import java.time.Duration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableWebMvc
public class SecurityConfig implements WebMvcConfigurer {

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http
//    , HandlerMappingIntrospector introspector
    ) throws Exception {


        // Disable  csrf
        http.csrf(AbstractHttpConfigurer::disable);

        // Authentication
        http.formLogin(auth -> auth.loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(customAuthenticationSuccessHandler)
                .failureUrl("/login?error"));
        http.logout(auth -> auth.logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout"));

//        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector).servletPath("/");

        // Authorization
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/home", "/guest", "/login", "/css/**", "/js/**", "/images/**", "/assets/**").permitAll()
                        .requestMatchers("/librarian/**").hasAnyRole(AccountEnum.Role.LIBRARIAN.name())
                        .requestMatchers("/lecturer/**").hasAnyRole(AccountEnum.Role.LECTURER.name())
                        .requestMatchers("/student/**").hasAnyRole(AccountEnum.Role.STUDENT.name())
//
//                        .requestMatchers(mvcMatcherBuilder.pattern("/")).permitAll()
//                        .requestMatchers(mvcMatcherBuilder.pattern("/home")).permitAll()
//                        .requestMatchers(mvcMatcherBuilder.pattern("/guest")).permitAll()
//                        .requestMatchers(mvcMatcherBuilder.pattern("/login")).permitAll()
//                        .requestMatchers(mvcMatcherBuilder.pattern("/ckfinder/*")).permitAll()

//                        .requestMatchers(new AntPathRequestMatcher("images/**")).permitAll()
//                        .requestMatchers(new AntPathRequestMatcher("css/**")).permitAll()
//                        .requestMatchers(new AntPathRequestMatcher("js/**")).permitAll()
//                        .requestMatchers(new AntPathRequestMatcher("assets/**")).permitAll()
                        // Auth detail
//                        .requestMatchers(mvcMatcherBuilder.pattern("/librarian/**")).hasAnyRole(AccountEnum.Role.LIBRARIAN.name())
//                        .requestMatchers(mvcMatcherBuilder.pattern("/lecturer/**")).hasAnyRole(AccountEnum.Role.LECTURER.name())
//                        .requestMatchers(mvcMatcherBuilder.pattern("/student/**")).hasAnyRole(AccountEnum.Role.STUDENT.name())
                        .anyRequest().authenticated())
        ;

        // Exception Handling
        http.exceptionHandling(auth -> auth.accessDeniedPage("/access_denied"));
        

        return http.build();
    }
}