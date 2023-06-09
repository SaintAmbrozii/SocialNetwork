package com.example.socialnetwork.config;

import com.example.socialnetwork.security.JwtTokenFilter;
import com.example.socialnetwork.security.RestAuthentificationEntryPoint;
import com.example.socialnetwork.service.UserDetailService;
import com.example.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {



    @Autowired
    private JwtTokenFilter jwtFilter;

    @Autowired
    RestAuthentificationEntryPoint authentificationEntryPoint;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailService userService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

 //   @Bean
 //   public AuthenticationProvider authenticationProvider() {
 //       DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userService);
//        authProvider.setPasswordEncoder(passwordEncoder);
//        return authProvider;
//    }

 //   @Bean
 //   public AuthenticationManager authManager(HttpSecurity http) throws Exception {
 //       return http.getSharedObject(AuthenticationManagerBuilder.class).authenticationProvider(authenticationProvider())
  //              .build();
 //   }
    @Bean
     public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder passwordEncoder,
                                          UserDetailService userDetailService) throws Exception {
     return http.getSharedObject(AuthenticationManagerBuilder.class).userDetailsService(userDetailService)
             .passwordEncoder(passwordEncoder).and().build();
 }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable().exceptionHandling().and()
                .authorizeHttpRequests()
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/**").permitAll()
                .requestMatchers("/js/**").permitAll()
                .requestMatchers("/download/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}
