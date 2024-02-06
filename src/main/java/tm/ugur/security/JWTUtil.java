package tm.ugur.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.ZonedDateTime;
import java.util.Date;


@Component
public class JWTUtil {

    @Value("${jwt_secret}")
    private String secret;

    public String generateToken(String userPhoneNumber)  {

        Date expirationDate = Date.from(ZonedDateTime.now().plusMonths(1).toInstant());

        return JWT.create()
                .withSubject("Client number phone")
                .withClaim("phone", userPhoneNumber)
                .withIssuedAt(new Date())
                .withIssuer("tm.takyk.ugur")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveClaim(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("Client number phone")
                .withIssuer("tm.takyk.ugur")
                .build();

        DecodedJWT jwt = verifier.verify(token);

        return jwt.getClaim("phone").asString();
    }
}
