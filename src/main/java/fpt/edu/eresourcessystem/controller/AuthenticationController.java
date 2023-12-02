package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.dto.UserLogDto;
import fpt.edu.eresourcessystem.enums.AccountEnum;
import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.model.UserLog;
import fpt.edu.eresourcessystem.service.AccountService;
import fpt.edu.eresourcessystem.service.UserLogService;
import fpt.edu.eresourcessystem.utils.RedirectUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {
    private final AccountService accountService;
    private final UserLogService userLogService;

    public AuthenticationController(AccountService accountService, UserLogService userLogService) {
        this.accountService = accountService;
        this.userLogService = userLogService;
    }

    private UserLog addUserLog(String url) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Account loggedInUser = accountService.findByEmail(currentPrincipalName);
        UserLog userLog = new UserLog(new UserLogDto(url,loggedInUser.getRole()));
        userLog = userLogService.addUserLog(userLog);
        return userLog;
    }

    @GetMapping({"/login"})
    public String loginView(Authentication authentication) {
        String redirect = getAuthenticatedUserRedirectUrl(authentication);
        if (redirect != null) {
            // log user action
            String currentPrincipalName = authentication.getName();
            Account loggedInUser = accountService.findByEmail(currentPrincipalName);
            UserLogDto userLogDto = new UserLogDto("/login", loggedInUser.getRole());
            UserLog userLog = userLogService.addUserLog(new UserLog(userLogDto));
            return redirect;
        }
        return "guest/guest_login";
    }

    @GetMapping({"/logout"})
    public String logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Account loggedInUser = accountService.findByEmail(currentPrincipalName);

        // log user action
        UserLogDto userLogDto = new UserLogDto("/logout", loggedInUser.getRole());
        UserLog userLog = userLogService.addUserLog(new UserLog(userLogDto) );
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

}
