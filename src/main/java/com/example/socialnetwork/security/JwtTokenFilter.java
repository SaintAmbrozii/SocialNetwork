package com.example.socialnetwork.security;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.service.UserDetailService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    protected final Log logger = LogFactory.getLog(getClass());

    private final JwtTokenGenerator jwtTokenGenerator;
    private final UserDetailService userService;

    public JwtTokenFilter(JwtTokenGenerator jwtTokenGenerator, UserDetailService userService) {
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = getTokenFromRequest((HttpServletRequest) request);
        String userName = null;
        if (token != null && token.startsWith("Bearer ")) {
            userName = jwtTokenGenerator.extractUsername(token);
            logger.info("AuthToken: "+ token);
        }

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userService.loadUserByUsername(userName);
            logger.info("UserName: "+userName);
                    // getUserDetails(token);
                    //userService.loadUserByUsername(userName);

            if (jwtTokenGenerator.validateToken(token, userDetails)) {

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
             SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
         //       TokenBasedAuthentification authentication = new TokenBasedAuthentification(userDetails);
         //      authentication.setToken(token);
         //      SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            else {
                logger.error("Something is wrong with Token.");
            }
        }
        filterChain.doFilter(request, response);


    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
    private UserDetails getUserDetails(String token) {
        User userDetails = new User();
        String username = jwtTokenGenerator.extractUsername(token);
        userDetails.setName(username);

        return (UserDetails) userDetails;
    }
}
