package tm.ugur.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class TestFiler extends BasicAuthenticationFilter {
    public TestFiler(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    public TestFiler(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);
    }
}
