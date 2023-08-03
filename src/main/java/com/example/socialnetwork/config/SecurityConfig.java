package com.example.socialnetwork.config;

import com.example.socialnetwork.domain.Role;
import com.example.socialnetwork.security.JwtTokenFilter;
import com.example.socialnetwork.security.JwtTokenGenerator;
import com.example.socialnetwork.security.RestAuthentificationEntryPoint;
import com.example.socialnetwork.service.UserDetailService;
import com.example.socialnetwork.service.UserService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true,securedEnabled = true,jsr250Enabled = true)
public class SecurityConfig {

    private static final String AUTHORIZATION = "Authorization";
    protected final Log logger = LogFactory.getLog(getClass());





    @Autowired
    RestAuthentificationEntryPoint authentificationEntryPoint;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailService userService;
    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

   // @Autowired
  //  private JwtTokenFilter jwtTokenFilter;


  //  @Bean
 //   public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
 //           throws Exception {
 //       return authenticationConfiguration.getAuthenticationManager();
 //   }

       @Bean
       public AuthenticationProvider authenticationProvider() {
           DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

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
                .anonymous(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/users/**").authenticated()
                .requestMatchers(AUTH_WHITELIST).permitAll()
                .requestMatchers("/js/**").permitAll()
                .requestMatchers("/download/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtTokenGenerator,userService), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }





    private static final String[] AUTH_WHITELIST = {"/swagger-resources/**", "/swagger-ui/**", "/v3/api-docs/**",
            "/webjars/**", "/", "/webjars/**", "/*.html", "favicon.ico", "/*/*.html"};

}
