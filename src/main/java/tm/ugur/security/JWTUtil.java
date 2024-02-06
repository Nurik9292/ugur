package tm.ugur.security;

import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;


@Component
public class JWTUtil {

    public String generateToken(String userPhoneNumber)  {

        Date expirationDate = Date.from(ZonedDateTime.now().plusMonths(1).toInstant());

        return "";
    }
}
