package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.enums.AccountEnum;
import fpt.edu.eresourcessystem.exception.AccountNotExistedException;
import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthenticationController {
    private final AccountService accountService;

    public AuthenticationController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping({"/", "/guest", "/login"})
    public String loginProcess(final Model model) throws AccountNotExistedException {
        model.addAttribute("account", new Account());
        model.addAttribute("roles", AccountEnum.Role.values());
        return "guest/guest_home";
    }

    @PostMapping({"/login"})
    public String login(@ModelAttribute Account account, @RequestParam String roles) {
        if (null == account) {
            return "redirect:/login";
        } else if ("LIBRARIAN".equals(roles)) {
            return "redirect:/librarian";
        } else if ("LECTURER".equals(roles)) {
            return "redirect:/lecturer";
        } else if ("STUDENT".equals(roles)) {
            return "redirect:/student";
        }
        return "redirect:/guest";
    }

    @GetMapping({"/logout"})
    public String logout() {
        return "redirect:/login";
    }

}
