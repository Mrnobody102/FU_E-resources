package fpt.edu.eresourcessystem.config.security;

import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.model.CustomOAuth2User;
import fpt.edu.eresourcessystem.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("customOAuth2UserService")
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final AccountRepository accountRepository;

    @Autowired
    public CustomOAuth2UserService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String clientName = userRequest.getClientRegistration().getClientName();
        OAuth2User user =  super.loadUser(userRequest);
        String email = user.<String>getAttribute("email");
        Account account = accountRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Account not found with email: " + email));
        // Granted Authority
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + account.getRole().toString());
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        OAuth2User oAuth2User = new CustomOAuth2User(user, clientName, authorities);
        return oAuth2User;
    }

}