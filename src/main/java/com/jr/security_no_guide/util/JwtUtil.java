package com.jr.security_no_guide.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;
@Component
public class JwtUtil {
    @Value("${security.jwt.key.private}")
    private String secretKey;

    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    public String createToken(Authentication authentication){
        Algorithm algorithm=Algorithm.HMAC256(this.secretKey);
        String username=authentication.getPrincipal().toString();
        String authorities=authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String jwtToken= JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(username)
                .withClaim("authorities",authorities)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis()+1800000))
                .withNotBefore(new Date(System.currentTimeMillis()))
                .withJWTId(UUID.randomUUID().toString())
                .sign(algorithm);

        System.out.println("Generated Token: " + jwtToken); // Log the generated token

        return jwtToken;
    }

    public DecodedJWT validateToken(String token) {
        try {
            System.out.println("Token to Validate: " + token);

            Algorithm algorithm = Algorithm.HMAC256(this.secretKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator)
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT;
        } catch (JWTVerificationException exception) {
            System.out.println("❌ Token inválido: " + exception.getMessage());
            throw new JWTVerificationException("token no valido"+exception.getMessage());
        }
    }



    public String extractUsername(DecodedJWT decodedJWT){
        return decodedJWT.getSubject();
    }

    public Claim extractSpecificClaim(String claimName,DecodedJWT decodedJWT){
        return decodedJWT.getClaim(claimName);
    }
}