package fpt.edu.eresourcessystem.service.security;

import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.repository.AccountRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        Account account = accountRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Account not found with email: " + email));

        // Granted Authority
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + account.getRole().name());
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        return new User(account.getEmail(), account.getPassword(), true, true, true, true, authorities);
        // IN CASE LOAD ACCOUNT FROM FPT GOOGLE MAIL



        // IN CASE LOAD ACCOUNT FROM FE-ID
    }
}
