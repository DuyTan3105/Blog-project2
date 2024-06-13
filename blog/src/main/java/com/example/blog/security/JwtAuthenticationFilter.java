package com.example.blog.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenHelper jwtTokenHelper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //getToken
        String bearertoken=request.getHeader("Authorization");
        log.info("{}",bearertoken);
        String userName = null;
        String token = null;
        if(bearertoken!=null && bearertoken.startsWith("Bearer ")) {
            token = bearertoken.substring(7);
            log.info("{}",token);
            try {
                userName = jwtTokenHelper.getUserNameFromToken(token);
            } catch (IllegalArgumentException ex) {
                System.out.println("Token is illegal");
            } catch (ExpiredJwtException ex) {
                System.out.println("Token is expired");
            } catch (UnsupportedJwtException ex) {
                System.out.println("Token is unsupported");
            } catch (MalformedJwtException ex) {
                System.out.println("Token is malformed");
            }
        } else {
            System.out.println("token is null or not start with Bearer");
        }

        if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            if(jwtTokenHelper.validateToken(token,userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                System.out.println("Invalid jwt token");
            }
        } else {
            System.out.println("username is null or context is not null");
        }
        filterChain.doFilter(request,response);
    }
}
