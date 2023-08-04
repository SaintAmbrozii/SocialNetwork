package com.example.socialnetwork.security;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.service.UserDetailService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    protected final Log logger = LogFactory.getLog(getClass());

    private final JwtTokenGenerator jwtTokenGenerator;
    private final UserDetailService userService;

    public JwtTokenFilter(JwtTokenGenerator jwtTokenGenerator, UserDetailService userService) {
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.userService = userService;
    }

 //   @Override
 //   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
 //       String token = getTokenFromRequest(request);
 //       String userName = null;
 //       if (token != null && token.startsWith("Bearer ")) {
       //     userName = jwtTokenGenerator.extractUsername(token);
//           userName = jwtTokenGenerator.getUsername(userName);
 //           logger.info("AuthToken: "+ token);
  //      }

  //      if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

   //         UserDetails userDetails = userService.loadUserByUsername(userName);
    //        logger.info("UserName: "+userName);
                    // getUserDetails(token);
                    //userService.loadUserByUsername(userName);

    //        if (jwtTokenGenerator.validateToken(token, userDetails)) {

    //                UsernamePasswordAuthenticationToken authenticationToken =
    //                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    //            authenticationToken
   //                     .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    //         SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    //       }
    //        else {
   //             logger.error("Something is wrong with Token.");
   //         }
   //     }
  //      filterChain.doFilter(request, response);


  //  }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        userEmail = jwtTokenGenerator.extractUsername(jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userService.loadUserByUsername(userEmail);

            if (jwtTokenGenerator.validateToken(jwt, userDetails) ) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}



