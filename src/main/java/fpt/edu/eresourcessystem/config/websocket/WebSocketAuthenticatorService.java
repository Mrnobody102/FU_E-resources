package fpt.edu.eresourcessystem.config.websocket;

import fpt.edu.eresourcessystem.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class WebSocketAuthenticatorService {

    private final AccountRepository accountRepository;
    // This method MUST return a UsernamePasswordAuthenticationToken instance, the spring security chain is testing it with 'instanceof' later on. So don't use a subclass of it or any other class
    public UsernamePasswordAuthenticationToken getAuthenticatedOrFail() throws AuthenticationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getName());
        Collection<? extends GrantedAuthority> auth = authentication.getAuthorities();
        System.out.println(authentication.getAuthorities().toString());
        if (null == auth) {
            throw  new AuthenticationCredentialsNotFoundException("User didn't authenticate.");
        } else {
            return new UsernamePasswordAuthenticationToken(
                    authentication.getPrincipal(),
                    null,
                    Collections.singleton((GrantedAuthority ) () -> authentication.getAuthorities().toString()) // MUST provide at least one role
            );
        }

    }
}
