package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.dto.AccountDTO;
import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.model.GooglePojo;
import fpt.edu.eresourcessystem.service.security.CustomizeUserDetailsService;
import fpt.edu.eresourcessystem.utils.GoogleUtils;
import fpt.edu.eresourcessystem.utils.RedirectUtil;
import fpt.edu.eresourcessystem.enums.AccountEnum;
import fpt.edu.eresourcessystem.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthenticationController {
    private final AccountService accountService;

    private final GoogleUtils googleUtils;

    public AuthenticationController(AccountService accountService, GoogleUtils googleUtils) {
        this.accountService = accountService;
        this.googleUtils = googleUtils;
    }

    @GetMapping({"/login"})
    public String loginView(final Model model, Authentication authentication) {
        model.addAttribute("campuses", AccountEnum.getListCampus());
        String redirect = getAuthenticatedUserRedirectUrl(authentication);
        if (redirect != null) return redirect;
        return "guest/guest_login";
    }

    @GetMapping({"/logout"})
    public String logout() {
        return "redirect:/login";
    }

    private String getAuthenticatedUserRedirectUrl(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String redirectUrl = RedirectUtil.getRedirectUrl(authentication);
            if (redirectUrl != null) {
                return "redirect:" + redirectUrl;
            }
        }
        return null;
    }

    @RequestMapping("/login-google")
    public String loginGoogle(HttpServletRequest request) throws ClientProtocolException, IOException {
        String code = request.getParameter("code");

        if (code == null || code.isEmpty()) {
            return "redirect:/login?google=error";
        }
        String accessToken = googleUtils.getToken(code);
        GooglePojo googlePojo = googleUtils.getUserInfo(accessToken);
//        UserDetails userDetail = googleUtils.buildUser(googlePojo);

        Account account = accountService.findByEmail(googlePojo.getEmail());

        if (account == null) {
            return "redirect:/access_denied";
        }
        // Granted Authority
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + account.getRole().name());
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);

        UserDetails userDetail = new User(account.getEmail(), "", true, true, true, true, authorities);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null,
                userDetail.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
//        System.out.println(authentication.getAuthorities());
        return "redirect:" +RedirectUtil.getRedirectUrl(authentication);
    }

}
