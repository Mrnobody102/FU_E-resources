package fpt.edu.eresourcessystem.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {
    private String oauth2ClientName;
    private OAuth2User oauth2User;
    private List<GrantedAuthority> authorities;


    public CustomOAuth2User(OAuth2User oauth2User, String oauth2ClientName, List<GrantedAuthority> authorities) {
        this.oauth2User = oauth2User;
        this.oauth2ClientName = oauth2ClientName;
        this.authorities = authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        System.out.println(oauth2User.<String>getAttribute("email"));
        return oauth2User.getAttribute("email");
    }

    public String getEmail() {
        return oauth2User.<String>getAttribute("email");
    }

    public String getOauth2ClientName() {
        return this.oauth2ClientName;
    }
}
