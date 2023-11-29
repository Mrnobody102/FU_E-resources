package fpt.edu.eresourcessystem.config.auditing;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserAuditing implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication uname = SecurityContextHolder.getContext().getAuthentication();
        if(uname != null) {
            return Optional.of(uname.getName());
        } else {
            return Optional.of("generated_by_system");
        }
    }

}