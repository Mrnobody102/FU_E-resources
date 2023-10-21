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
public class InitController {
    private final AccountService accountService;

    public InitController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping({"/", "/guest","/home"})
    public String goHomePage(final Model model) throws AccountNotExistedException {
        return "guest/guest_home";
    }

    @GetMapping({"/access_denied"})
    public String accessDenied() {
        return "exception/404";
    }
}
