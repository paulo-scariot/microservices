package br.com.microservices.productapi.modules.jwt.service;

import br.com.microservices.productapi.config.exception.AuthorizationException;
import br.com.microservices.productapi.modules.jwt.dto.JwtResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class JwtService {

    private static final String EMPTY_SPACE = " ";
    private static final Integer TOKEN_INDEX = 1;

    @Value("${app-config.secrets.api-secret}")
    private String apiSecret;

    public void validateToken(String token) {

        var accessToken = extractToken(token);
        try{
            var claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(apiSecret.getBytes()))
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
            var user = JwtResponse.getUser(claims);
            if (isEmpty(user) || isEmpty(user.getId())) {
                throw new AuthorizationException("Token invalid");
            }
        } catch (Exception e){
            throw new AuthorizationException("Error while trying to process token");
        }
    }

    private String extractToken(String token) {
        if (isEmpty(token)){
            throw new AuthorizationException("Token is empty");
        }
        if (token.contains(EMPTY_SPACE)) {
            return token.split(EMPTY_SPACE)[TOKEN_INDEX];
        }
        return token;
    }
}
