package org.kash.jwtsecurityapi.filter;

import java.io.IOException;
import java.security.SignatureException;

import org.kash.jwtsecurityapi.service.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Component  
@RequiredArgsConstructor 
public class JwtAuthenticationFilter extends OncePerRequestFilter{


    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        try{
            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            final String jwt;
            final String userName;

            if(authHeader == null || !authHeader.startsWith("Bearer")){
                filterChain.doFilter(request, response);
                return;
            }

            // Bearer <Token>
            jwt = authHeader.substring(7);            

            if(!jwtService.isValidToken(jwt)){
                filterChain.doFilter(request, response);
                return;
            }

            userName = jwtService.extractUserNameFromToken(jwt); 

            if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
                var userDetails = userDetailsService.loadUserByUsername(userName);

                if(jwtService.isValidTokenForUser(jwt, userDetails)){
                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                    securityContext.setAuthentication(authToken);
                    SecurityContextHolder.setContext(securityContext);
                }
            }
               
        } catch(SignatureException e) {
            log.error("Invalid JWT Sigature: {}", e.getMessage(), e);
        } catch(MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage(), e);
        } catch(ExpiredJwtException e) {
            log.error("JWT token expired: {}", e.getMessage(), e);
        } catch(UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage(), e);
        } catch(IllegalArgumentException e) {
            log.error("JWT claims is empty: {}", e.getMessage(), e);
        } catch(Exception e) {
            log.error("JWT geneic exeption: {}", e.getMessage(), e);
        }
    
        filterChain.doFilter(request, response);
    }

}
