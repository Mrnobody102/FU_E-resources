package fpt.edu.eresourcessystem.controller.advices;

import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.service.AccountService;
import fpt.edu.eresourcessystem.service.NotificationService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    public final AccountService accountService;
    public final NotificationService notificationService;

    public GlobalControllerAdvice(AccountService accountService, NotificationService notificationService) {
        this.accountService = accountService;
        this.notificationService = notificationService;
    }

    @ModelAttribute("loggedInUser")
    public Account addLoggedInUserToModel() {
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return accountService.findByEmail(loggedInEmail);
    }

    @ModelAttribute("notificationNumber")
    public String getNotificationNumber() {
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        int numOfNotifications = notificationService.findUnreadByToAccount(loggedInEmail).size();
        return String.valueOf(numOfNotifications);
    }

    @ModelAttribute("allNotificationNumber")
    public String getAllNotificationNumber() {
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        int numOfNotifications = notificationService.findAllByToAccount(loggedInEmail).size();
        return String.valueOf(numOfNotifications);
    }

    public Account getLoggedInAccount() {
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (null == loggedInEmail || "anonymousUser".equals(loggedInEmail)) {
            return null;
        }
        return accountService.findByEmail(loggedInEmail);
    }
}
