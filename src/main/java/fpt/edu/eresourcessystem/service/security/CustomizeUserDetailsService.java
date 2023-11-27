package fpt.edu.eresourcessystem.service.security;

import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.repository.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("customizeUserDetailService")
public class CustomizeUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public CustomizeUserDetailsService(AccountRepository accountRepository) {
        super();
        this.accountRepository = accountRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // IN CASE LOAD ACCOUNT IS SELF-GENERATED FROM THE SYSTEM

        // IN CASE LOAD ACCOUNT FROM FPT GOOGLE MAIL

        // IN CASE LOAD ACCOUNT FROM FE-ID

        return getUserDetailsCustom(email);
    }

    private UserDetailsCustom getUserDetailsCustom(String email){
        Account user = accountRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Account not found with email: " + email));

        // Granted Authority
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        return new UserDetailsCustom(
                user.getUsername(),
                user.getPassword(),
                authorities,
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired()
        );
    }
}
