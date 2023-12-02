package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.dto.UserLogDto;
import fpt.edu.eresourcessystem.enums.AccountEnum;
import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.model.UserLog;
import fpt.edu.eresourcessystem.service.AccountService;
import fpt.edu.eresourcessystem.service.NotificationService;
import fpt.edu.eresourcessystem.service.UserLogService;
import fpt.edu.eresourcessystem.utils.RedirectUtil;
import org.springframework.context.annotation.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static fpt.edu.eresourcessystem.constants.UrlConstants.ACCESS_DENIED;

@Controller
public class AuthenticationController {
    private final AccountService accountService;
    private final UserLogService userLogService;
    private final NotificationService notificationService;

    public AuthenticationController(AccountService accountService, UserLogService userLogService, NotificationService notificationService) {
        this.accountService = accountService;
        this.userLogService = userLogService;
        this.notificationService = notificationService;
    }

    @GetMapping({"/", "/home"})
    public String goHomePage() {
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (null == loggedInEmail || "anonymousUser".equals(loggedInEmail)) {
            return "redirect:/guest";
        }
        Account loggedInAccount = accountService.findByEmail(loggedInEmail);
        if (loggedInAccount.getRole() == AccountEnum.Role.ADMIN) {

            return "redirect:/admin";

        } else if (loggedInAccount.getRole() == AccountEnum.Role.LIBRARIAN) {

            return "redirect:/librarian";

        } else if (loggedInAccount.getRole() == AccountEnum.Role.LECTURER) {

            return "redirect:/lecturer";

        } else if (loggedInAccount.getRole() == AccountEnum.Role.STUDENT) {

            return "redirect:/student";

        }
        return ACCESS_DENIED;
    }

    @GetMapping({"/login"})
    public String loginView(Authentication authentication) {
        String redirect = getAuthenticatedUserRedirectUrl(authentication);
        if (redirect != null) {
            // log user action
            UserLogDto userLogDto = new UserLogDto("/login");
            userLogService.addUserLog(new UserLog(userLogDto));
            return redirect;
        }
        return "guest/guest_login";
    }

    @GetMapping({"/logout"})
    public String logout() {
        // log user action
        UserLogDto userLogDto = new UserLogDto("/logout");
        userLogService.addUserLog(new UserLog(userLogDto));
        return "redirect:/login";
    }

    private String getAuthenticatedUserRedirectUrl(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String redirectUrl = RedirectUtil.getRedirectUrl(authentication);
            return "redirect:" + redirectUrl;
        }
        return null;
    }

}
