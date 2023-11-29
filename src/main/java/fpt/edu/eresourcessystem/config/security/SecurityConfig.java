package fpt.edu.eresourcessystem.config.security;

import fpt.edu.eresourcessystem.enums.AccountEnum;
import fpt.edu.eresourcessystem.service.security.CustomizeUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableWebMvc
public class SecurityConfig implements WebMvcConfigurer {

    private CustomizeUserDetailsService customizeUserDetailsService;

    private CustomizeAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    public SecurityConfig(CustomizeUserDetailsService customizeUserDetailsService, CustomizeAuthenticationSuccessHandler customAuthenticationSuccessHandler, CustomOAuth2UserService customOAuth2UserService) {
        this.customizeUserDetailsService = customizeUserDetailsService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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



        http.oauth2Login(auth -> auth.loginPage("/login")
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(customOAuth2UserService)
			    )
                .successHandler(customAuthenticationSuccessHandler)
//                .defaultSuccessUrl("/student", true)
                .failureUrl("/login?error"));

        http.logout(auth -> auth.logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout"));



//        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector).servletPath("/");

        // Authorization
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/","/contact_us", "/faq", "/home", "/guest", "/login", "/css/**", "/js/**", "/images/**", "/assets/**", "/oauth2/authorization/**").permitAll()
                        .requestMatchers("/admin/**").hasAnyRole(AccountEnum.Role.ADMIN.name())
                        .requestMatchers("/librarian/**").hasAnyRole(AccountEnum.Role.ADMIN.name(), AccountEnum.Role.LIBRARIAN.name())
                        .requestMatchers("/lecturer/**").hasAnyRole(AccountEnum.Role.ADMIN.name(), AccountEnum.Role.LIBRARIAN.name(), AccountEnum.Role.LECTURER.name())
                        .requestMatchers("/student/**").hasAnyRole(AccountEnum.Role.ADMIN.name(), AccountEnum.Role.LIBRARIAN.name(), AccountEnum.Role.LECTURER.name(), AccountEnum.Role.STUDENT.name())
                        .anyRequest().authenticated())
//                        .anyRequest().permitAll());
        ;

        // Exception Handling
        http.exceptionHandling(auth -> auth.accessDeniedPage("/access_denied"));
//        http.exceptionHandling(auth -> auth.accessDeniedPage("/login"));

        return http.build();
    }
}