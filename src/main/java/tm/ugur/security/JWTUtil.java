package tm.ugur.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {

    @Value("${jwt_secret}")
    private String secret;

    private final static Logger logger = LoggerFactory.getLogger(JWTUtil.class);

    public String generateToken(String userPhone) throws JWTVerificationException {
        Date expirationDate = Date.from(ZonedDateTime.now().plusDays(30).toInstant());

        return JWT.create()
                .withSubject("Client details")
                .withClaim("phone", userPhone)
                .withIssuedAt(new Date())
                .withIssuer("tmugurtakyk")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveClaim(String token){
        String phone = "";
        try{
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                    .withSubject("Client details")
                    .withIssuer("tmugurtakyk")
                    .build();

            DecodedJWT jwt = verifier.verify(token);
            phone = jwt.getClaim("phone").asString();
        }catch (Exception e){
            logger.error("Valid token error: " + e.getMessage());
        }

        return phone;
    }
}
