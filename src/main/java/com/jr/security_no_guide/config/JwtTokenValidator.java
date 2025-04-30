package com.jr.security_no_guide.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.jr.security_no_guide.util.JwtUtil;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

public class JwtTokenValidator extends OncePerRequestFilter {

    private JwtUtil jwtUtil;
    @Autowired
    public JwtTokenValidator(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {


        String jwtToken=request.getHeader(HttpHeaders.AUTHORIZATION);

        if(jwtToken!=null){
            jwtToken=jwtToken.substring(7);
            DecodedJWT decodedJWT= jwtUtil.validateToken(jwtToken);
            String username=jwtUtil.extractUsername(decodedJWT);
            String authoritiesString=jwtUtil.extractSpecificClaim("authorities",decodedJWT).asString();

            Collection<? extends GrantedAuthority>authorities= AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesString);

            SecurityContext context= SecurityContextHolder.createEmptyContext();
            Authentication authentication=new UsernamePasswordAuthenticationToken(username,null,authorities);
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
        }
        filterChain.doFilter(request ,response);
    }
}
