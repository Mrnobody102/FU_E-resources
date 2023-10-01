package fpt.edu.eresourcessystem.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import fpt.edu.eresourcessystem.service.AccountService;
import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.exception.AccountNotExistedException;

@Controller
@PropertySources(value = {@PropertySource("classpath:webconfig.properties")})
public class AccountController {
    @Value("${page-size}")
    private Integer pageSize;    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/login")
    public String login(@ModelAttribute Account account) throws AccountNotExistedException {
        if(account != null){
            return "course/detail_course";
        } else throw new AccountNotExistedException("Account not existed.");
    }
}
