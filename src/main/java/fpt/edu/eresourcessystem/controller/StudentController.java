package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.exception.AccountNotExistedException;
import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.service.AccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@PropertySources(value = {@PropertySource("constants.properties")})
public class StudentController {
    @Value("${page-size}")
    private Integer pageSize;
    private final AccountService accountService;

    public StudentController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/login")
    public String login(@ModelAttribute Account account) throws AccountNotExistedException {
        if(account != null){
            return "student";
        } else throw new AccountNotExistedException("Account not existed.");
    }
}
